
import { useRef, useEffect } from "react";
import { createTaskAssignment } from "../api/taskApi";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin, { Draggable } from "@fullcalendar/interaction";

/**
 * CalendarComponent displays the main calendar and handles drag-and-drop task assignment.
 */
export default function CalendarComponent({ events, onEventDrop, onEventResize, tasks, selectedStaff, onTaskAssigned, staffColorMap, calendarView, onViewDatesChange }) {
  const calendarRef = useRef(null);

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


  function renderEventContent(eventInfo) {
    let staffFirstName = eventInfo.event.extendedProps.staffFirstName;
    let staffLastName = eventInfo.event.extendedProps.staffLastName;
    let staffName = "";
    if (staffFirstName || staffLastName) {
      staffName = (staffFirstName && staffLastName) ? `${staffFirstName} ${staffLastName}` : (staffFirstName || staffLastName);
    } else if (eventInfo.event.extendedProps.staffName) {
      staffName = eventInfo.event.extendedProps.staffName;
    }
    const task = getTaskById(tasks, eventInfo.event.extendedProps.taskId);
    const taskName = task ? task.name : eventInfo.event.title;
    const staffId = eventInfo.event.extendedProps.staffId;
    const bgColor = staffColorMap && staffId ? staffColorMap[staffId] : '#e3f7ef';
    return (
      <div style={{ background: bgColor, borderRadius: 6, padding: '2px 4px', color: '#2e363f' }}>
        <b>
          {staffName ? staffName + ": " : ""}
          {taskName}
        </b>
      </div>
    );
  }


  function handleEventClick(info) {
    const { extendedProps } = info.event;
    let staffFirstName = extendedProps.staffFirstName;
    let staffLastName = extendedProps.staffLastName;
    let staffName = (staffFirstName && staffLastName) ? `${staffFirstName} ${staffLastName}` : (staffFirstName || staffLastName || "");
    let taskName = extendedProps.taskName;
    let taskDescription = extendedProps.taskDescription;

    const task = getTaskById(tasks, extendedProps.taskId);
    taskName = task ? task.name : taskName;
    taskDescription = task ? task.description : taskDescription;
    let msg = "";
    if (staffName) msg += `Staff: ${staffName}\n`;
    msg += `Task: ${taskName || info.event.title}`;
    if (taskDescription) msg += `\nDescription: ${taskDescription}`;
    alert(msg);
  }

  function getTaskById(tasks, id) {
    return tasks && id ? tasks.find((t) => String(t.id) === String(id)) : undefined;
  }

  useEffect(() => {
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
        eventReceive={async (info) => {
          info.event.remove();

          const { taskId, staffId, staffName } = info.event.extendedProps;

          let start = info.event.start;
          let end = info.event.end;
          if (!end && start) {
            end = new Date(start.getTime() + 60 * 60 * 1000);
          }

          if (taskId && staffId) {
            if (window.__assignmentInProgress) return;
            window.__assignmentInProgress = true;
            try {
              const assignment = await createTaskAssignment({
                taskId: Number(taskId),
                staffId: Number(staffId),
                timeRange: {
                  start: start?.toISOString(),
                  end: end?.toISOString(),
                }
              });
              if (assignment && staffName && !assignment.staffName) {
                assignment.staffName = staffName;
              }
              const task = tasks && Array.isArray(tasks) ? tasks.find(t => String(t.id) === String(taskId)) : undefined;
              const removeAfterAssign = localStorage.getItem("removeAfterAssign") === "true";
              if (onTaskAssigned) await onTaskAssigned(assignment, task, removeAfterAssign);
            } catch (err) {
              alert("TaskAssignment konnte nicht erstellt werden!");
            } finally {
              window.__assignmentInProgress = false;
            }
          } else {
            alert("Bitte zuerst einen Mitarbeiter auswählen und dann Task ziehen!");
          }
        }}
        eventContent={renderEventContent}
        eventClick={handleEventClick}
        selectable={true}
  eventResizableFromStart={true}
  allDaySlot={false}
      />
    </div>
  );
}
