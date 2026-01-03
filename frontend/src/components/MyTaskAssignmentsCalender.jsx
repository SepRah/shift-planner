import { useEffect, useMemo, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import {fetchTaskAssignmentsByStaffId} from "../api/taskApi.js";
import {getMe} from "../api/userAccountApi.js";

export default function TaskAssignmentList() {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);


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
    }, []);

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
        <div style={{ width: "100%" }}>
            <FullCalendar
                plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
                initialView="timeGridWeek"
                firstDay={1}
                scrollTime="07:00:00"
                allDaySlot={false}
                nowIndicator={true}
                events={events}
                eventClick={handleEventClick}
            />
        </div>
    );
}
