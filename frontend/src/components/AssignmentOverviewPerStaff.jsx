import React from "react";


/**
 * Overview of all assignments per staff member for the current calendar view.
 * Shows for each person the total assigned time and all tasks with time & qualification level.
 * @author Benjamin Traffelet
 */
export default function AssignmentOverviewPerStaff({ events, tasks, staff, calendarDates }) {
  
  const viewStart = calendarDates.start ? new Date(calendarDates.start) : null;
  const viewEnd = calendarDates.end ? new Date(calendarDates.end) : null;

  /**
   * Checks if an event is within the current calendar view.
   */
  function isInView(ev) {
    const start = ev.start ? new Date(ev.start) : null;
    if (!start || !viewStart || !viewEnd) return false;
    return start < viewEnd && (ev.end ? new Date(ev.end) : start) > viewStart;
  }

  const staffMap = {};
  events.filter(isInView).forEach(ev => {
    const sid = ev.extendedProps?.staffId;
    const tid = ev.extendedProps?.taskId;
    const start = ev.start ? new Date(ev.start) : null;
    const end = ev.end ? new Date(ev.end) : null;
    if (!sid || !tid || !start || !end) return;
    const foundStaff = staff.find(s => String(s.id) === String(sid));
    const foundTask = tasks.find(t => String(t.id) === String(tid));
    if (!staffMap[sid]) {
      staffMap[sid] = {
        staff: foundStaff || { id: sid, firstName: 'Staff', lastName: sid },
        totalTime: 0,
        taskTimes: {}
      };
    }
    const duration = (end - start) / 3600000;
    staffMap[sid].totalTime += duration;
    if (!staffMap[sid].taskTimes[tid]) {
      staffMap[sid].taskTimes[tid] = { time: 0, task: foundTask || { id: tid, name: ev.extendedProps?.taskName || `Task ${tid}`, qualificationLevel: '?' } };
    }
    staffMap[sid].taskTimes[tid].time += duration;
  });

  return (
    <div style={{ marginTop: 24 }}>
      <h4>Assignments pro Staff</h4>
      {Object.values(staffMap)
        .filter(({ totalTime }) => totalTime > 0)
        .map(({ staff, totalTime, taskTimes }) => (
          <div key={staff.id} style={{ marginBottom: 16, padding: 8, background: '#f7fafd', borderRadius: 6 }}>
            <strong>{staff.firstName} {staff.lastName}</strong>
            <div>Total time: <b>{totalTime.toFixed(2)}h</b></div>
            <div style={{ marginLeft: 12 }}>
              {Object.values(taskTimes).map(({ time, task }) => (
                <div key={task.id}>
                  {task.name}
                  <span style={{ color: '#5a8d6dff', fontSize: 'smaller', marginLeft: 6 }}>[{task.qualificationLevel}]</span>: {time.toFixed(2)}h
                </div>
              ))}
            </div>
          </div>
        ))}
    </div>
  );
}
