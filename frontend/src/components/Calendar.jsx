import React, { useRef, useEffect } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin, { Draggable } from "@fullcalendar/interaction";

// Hilfsfunktion, um Task-Infos anhand der ID zu finden
function getTaskById(tasks, id) {
  return tasks && id ? tasks.find((t) => String(t.id) === String(id)) : undefined;
}

export default function CalendarComponent({ events, onEventDrop, onEventResize, tasks, selectedStaff, onTaskAssigned }) {
  const calendarRef = useRef(null);

  // Tasks draggable machen, wenn das Tasks-Element existiert
  useEffect(() => {
    const taskListEl = document.querySelector(".task-draggable-list");
    if (taskListEl) {
      new Draggable(taskListEl, {
        itemSelector: ".fc-draggable-task",
        eventData: function (taskEl) {
          return {
            title: taskEl.getAttribute("data-title"),
            extendedProps: {
              taskId: taskEl.getAttribute("data-task-id"),
              staffId: taskEl.getAttribute("data-staff-id"),
              staffName: taskEl.getAttribute("data-staff-name"),
            }
          };
        }
      });
    }
  }, [tasks, selectedStaff]);

  // Custom rendering for event content (shows staffName and taskName)
  function renderEventContent(eventInfo) {
    // staffName aus extendedProps oder aus events-Array holen
    let staffName = eventInfo.event.extendedProps.staffName;
    // Fallback: staffName aus events-Array suchen, falls nicht gesetzt
    if (!staffName && events && eventInfo.event.id) {
      const found = events.find(ev => String(ev.id) === String(eventInfo.event.id));
      if (found && found.extendedProps && found.extendedProps.staffName) {
        staffName = found.extendedProps.staffName;
      }
    }
    const task = getTaskById(tasks, eventInfo.event.extendedProps.taskId);
    return (
      <div>
        <b>
          {staffName ? staffName + ": " : ""}
          {task ? task.name : eventInfo.event.title}
        </b>
      </div>
    );
  }

  // Show description on event click
  function handleEventClick(info) {
    const { extendedProps } = info.event;
    const task = getTaskById(tasks, extendedProps.taskId);
    let msg = "";
    if (extendedProps.staffName) msg += `Mitarbeiter: ${extendedProps.staffName}\n`;
    msg += `Task: ${task ? task.name : info.event.title}`;
    if (task && task.description) msg += `\nBeschreibung: ${task.description}`;
    alert(msg);
  }

  return (
    <div style={{ flex: 1 }}>
      <FullCalendar
        ref={calendarRef}
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
        initialView="timeGridWeek"
        editable={true}
        droppable={true}
        events={events}
        eventDrop={(info) => onEventDrop && onEventDrop(info.event)}
        eventResize={(info) => onEventResize && onEventResize(info.event)}
        eventAdd={null}
        eventReceive={async (info) => {
          // Remove event immediately to prevent double POST
          info.event.remove();

          const { taskId, staffId } = info.event.extendedProps;

          // Set default end time if not set (+1 hour)
          let start = info.event.start;
          let end = info.event.end;
          if (!end && start) {
            end = new Date(start.getTime() + 60 * 60 * 1000);
          }

          if (taskId && staffId) {
            const res = await fetch("http://localhost:8080/api/task-assignments", {
              method: "POST",
              headers: {
                Authorization: localStorage.getItem("token") ? `Bearer ${localStorage.getItem("token")}` : undefined,
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                taskId: Number(taskId),
                staffId: Number(staffId),
                timeRange: {
                  start: start?.toISOString(),
                  end: end?.toISOString(),
                }
              }),
            });
            if (res.ok) {
              const assignment = await res.json();
              if (onTaskAssigned) onTaskAssigned(assignment);
            } else {
              alert("TaskAssignment konnte nicht erstellt werden!");
            }
          } else {
            alert("Bitte zuerst einen Mitarbeiter auswählen und dann Task ziehen!");
          }
        }}
        eventContent={renderEventContent}
        eventClick={handleEventClick}
        selectable={true}
        eventResizableFromStart={true}
      />
    </div>
  );
}
