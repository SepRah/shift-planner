import {useEffect, useMemo, useRef, useState} from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import {fetchTaskAssignmentsByStaffId} from "../api/taskApi.js";
import {getMe} from "../api/userAccountApi.js";

export default function TaskAssignmentList({calendarView, onViewDatesChange}) {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const calendarRef = useRef(null);


    async function loadMyTaskAssignments() {
        setLoading(true);
        setError(null);
        try {
            const me = await getMe();
            const data = await fetchTaskAssignmentsByStaffId(me.id);
            setItems(Array.isArray(data) ? data : []);
        } catch (err) {
            const msg = err?.response?.data?.message ??
                err?.message ??
                "Unknown error while loading task assignments";
            setError(msg);
            setItems([]);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        document.title = "My Tasks";
        loadMyTaskAssignments();
        if (calendarRef.current && calendarView) {
            setTimeout(() => {
                const api = calendarRef.current.getApi?.();
                if (api) {
                    if (api.view?.type !== calendarView) {
                        api.changeView(calendarView);
                    }
                    if (onViewDatesChange && api.view) {
                        onViewDatesChange({ start: api.view.activeStart, end: api.view.activeEnd });
                    }
                }
            }, 0);
        }
    }, [calendarView, onViewDatesChange]);

    const events = useMemo(() => {
        return (items ?? [])
            .filter((a) => a?.timeRange?.start) // ohne Start kann FullCalendar nicht sinnvoll platzieren
            .map((a) => ({
                id: String(a.id),
                title: a.taskName ?? "Task",
                start: a.timeRange.start, // ISO string ok
                end: a.timeRange.end ?? null,
                // alles was du später brauchst:
                extendedProps: {
                    description: a.taskDescription ?? "",
                    completed: !!a.completed,
                    taskName: a.taskName,
                },
            }));
    }, [items]);

    function handleEventClick(info) {
        const { description, completed } = info.event.extendedProps || {};
        alert(
            `${info.event.title}\n` +
            (description ? `Beschreibung: ${description}\n` : "") +
            (completed ? "Status: Completed" : "Status: Open")
        );
    }

    if (loading) return <p>Load Tasks…</p>;
    if (error) return <p style={{ color: "crimson" }}>Error: {error}</p>;
    if (items.length === 0) return <p>No Tasks existing.</p>;



    return (
        <div style={{ flex: 1 }}>
            <FullCalendar
                ref={calendarRef}
                plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
                initialView={calendarView || "timeGridWeek"}
                editable={true}
                droppable={true}
                events={events}
                eventDrop={(info) => onEventDrop && onEventDrop(info.event)}
                eventResize={(info) => onEventResize && onEventResize(info.event)}
                datesSet={(info) => {
                    if (onViewDatesChange) {
                        onViewDatesChange({ start: info.start, end: info.end });
                    }
                }}
                eventClick={handleEventClick}
                allDaySlot={false}
                eventResizableFromStart={true}
            />
        </div>
    );
}
