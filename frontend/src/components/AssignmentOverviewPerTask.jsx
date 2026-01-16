
/**
 * Overview of all assignments per task for the current calendar view.
 * Shows for each task the total assigned time and all staff with time & qualification level.
 * @author Benjamin Traffelet
 */
export default function AssignmentOverviewPerTask({ events, tasks, staff, calendarDates }) {
  const viewStart = calendarDates.start ? new Date(calendarDates.start) : null;
  const viewEnd = calendarDates.end ? new Date(calendarDates.end) : null;

  function isInView(ev) {
    const start = ev.start ? new Date(ev.start) : null;
    if (!start || !viewStart || !viewEnd) return false;
    return start < viewEnd && (ev.end ? new Date(ev.end) : start) > viewStart;
  }

  // Build a map: taskId -> { task, totalTime, staffTimes }
  const taskMap = {};
  events.filter(isInView).forEach(ev => {
    const tid = ev.extendedProps?.taskId;
    const sid = ev.extendedProps?.staffId;
    const start = ev.start ? new Date(ev.start) : null;
    const end = ev.end ? new Date(ev.end) : null;
    if (!tid || !sid || !start || !end) return;
    const foundTask = tasks.find(t => String(t.id) === String(tid));
    const foundStaff = staff.find(s => String(s.id) === String(sid));
    if (!taskMap[tid]) {
      taskMap[tid] = {
        task: foundTask || { id: tid, name: `Task ${tid}`, qualificationLevel: '?' },
        totalTime: 0,
        staffTimes: {}
      };
    }
    const duration = (end - start) / 3600000;
    taskMap[tid].totalTime += duration;
    if (!taskMap[tid].staffTimes[sid]) {
      taskMap[tid].staffTimes[sid] = { time: 0, staff: foundStaff || { id: sid, firstName: 'Staff', lastName: sid } };
    }
    taskMap[tid].staffTimes[sid].time += duration;
  });

  return (
    <div style={{ marginTop: 24 }}>
      <h4>Assignments per Task</h4>
      {Object.values(taskMap)
        .filter(({ totalTime }) => totalTime > 0)
        .map(({ task, totalTime, staffTimes }) => (
          <div key={task.id} style={{ marginBottom: 16, padding: 8, background: '#f7fafd', borderRadius: 6 }}>
            <strong>{task.name}</strong>
            <span style={{ color: '#5a8d6dff', fontSize: 'smaller', marginLeft: 6 }}>[{task.qualificationLevel}]</span>
            <div>Total time: <b>{totalTime.toFixed(2)}h</b></div>
            <div style={{ marginLeft: 12 }}>
              {Object.values(staffTimes).map(({ time, staff }) => (
                <div key={staff.id}>
                  {staff.firstName} {staff.lastName}: {time.toFixed(2)}h
                </div>
              ))}
            </div>
          </div>
        ))}
    </div>
  );
}
