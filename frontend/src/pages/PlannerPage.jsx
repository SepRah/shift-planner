import { useEffect, useState, useMemo } from "react";

import StaffList from "../components/StaffList";
import AssignmentOverviewPerStaff from "../components/AssignmentOverviewPerStaff";
import AssignmentOverviewPerTask from "../components/AssignmentOverviewPerTask";
import TaskList from "../components/TaskList";
import { getTasks, updateTask, fetchAllTaskAssignments, updateTaskAssignment, getAllTasksInclInactive, updateTaskActiveStatus } from "../api/taskApi";
import { getStaffMembers } from "../api/staffApi";
import Calendar from "../components/Calendar";
import Navbar from "../components/Navbar";
import "../styles/PlannerPage.css";


/**
 * Maps backend assignments to FullCalendar event objects.
 * @param {Array} assignments
 * @returns {Array}
 */
function mapAssignmentsToEvents(assignments) {
  return assignments.map((a) => ({
    id: a.id,
    title: a.taskName || '',
    start: a.timeRange?.start,
    end: a.timeRange?.end,
    extendedProps: {
      assignmentId: a.id,
      staffId: a.staffId,
      staffFirstName: a.staffFirstName,
      staffLastName: a.staffLastName,
      taskId: a.taskId,
      taskName: a.taskName,
      taskDescription: a.taskDescription,
    },
  }));
}

  /**
 * Generates a nice color for each staff member for calendar display.
 * @param {number|string} staffId - Staff ID
 * @param {number} index - Index in staff array
 * @returns {string} Color code
 */
function getStaffColor(staffId, index) {
  const palette = [
    '#FFECB3', '#B3E5FC', '#C8E6C9', '#FFCDD2', '#D1C4E9', '#FFF9C4',
    '#B2DFDB', '#F8BBD0', '#DCEDC8', '#FFE0B2', '#F0F4C3', '#B3E5FC'
  ];
  return palette[index % palette.length];
}

/**
 * Main planner page: shows staff, tasks, calendar, and assignment overviews.
 */
export default function PlannerPage() {
  const [staff, setStaff] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [allTasksInclInactive, setAllTasksInclInactive] = useState([]);
  const [events, setEvents] = useState([]);
  const [selectedStaff, setSelectedStaff] = useState(null);
  const [calendarView, setCalendarView] = useState("timeGridWeek");
  const [calendarDates, setCalendarDates] = useState({ start: null, end: null });

  const staffColorMap = useMemo(() => {
    const map = {};
    staff.forEach((s, idx) => {
      if (s && s.id != null) map[s.id] = getStaffColor(s.id, idx);
    });
    return map;
  }, [staff]);

  /**
   * Loads staff, tasks, and assignments from API when page loads.
   */
  useEffect(() => {
    document.title = "Shiftplanner – Planner";
    getStaffMembers()
      .then(setStaff)
      .catch(() => setStaff([]));
    getTasks()
      .then(setTasks)
      .catch(() => setTasks([]));
    getAllTasksInclInactive()
      .then(setAllTasksInclInactive)
      .catch(() => setAllTasksInclInactive([]));
    fetchAllTaskAssignments()
      .then((assignments) => {
        const mappedEvents = assignments.map((a) => ({
          id: a.id,
          title: a.taskName || '',
          start: a.timeRange?.start,
          end: a.timeRange?.end,
          extendedProps: {
            assignmentId: a.id,
            staffId: a.staffId,
            staffFirstName: a.staffFirstName,
            staffLastName: a.staffLastName,
            taskId: a.taskId,
            taskName: a.taskName,
            taskDescription: a.taskDescription,
          },
        }));
        setEvents(mappedEvents);
      })
      .catch(() => setEvents([]));
  }, []);

  /**
   * Reloads all tasks (active and inactive) from API.
   */
  const reloadTasks = async () => {
    try {
      const allTasks = await getTasks();
      setTasks(allTasks);
      const allInclInactive = await getAllTasksInclInactive();
      setAllTasksInclInactive(allInclInactive);
    } catch {
      setTasks([]);
      setAllTasksInclInactive([]);
    }
  };

  /**
   * Handler for when a new task is created.
   */
  const handleTaskCreated = async () => {
    await reloadTasks();
  };


  /**
   * Sets a task to inactive in the backend and reloads the task list.
   * @param {Object} task - The task to deactivate
   */
  const deactivateTask = async (task) => {
    if (!task || !task.id) return;
    const payload = { ...task, active: false };
    if (task.active === 0 || task.active === 1) {
      payload.active = 0;
    }
    await updateTask(task.id, payload);
    await reloadTasks();
  };

  /**
   * Handler for drag & drop or resize of calendar events.
   * Updates assignment in backend and reloads assignments and tasks.
   * @param {Object} event - The calendar event
   */
  const handleEventChange = async (event) => {
    const assignmentId = event.extendedProps.assignmentId || event.id;
    if (!assignmentId) return;
    try {
      await updateTaskAssignment(assignmentId, {
        timeRange: {
          start: event.start?.toISOString(),
          end: event.end?.toISOString(),
        },
      });
    } catch {
      alert("Error of updating the TaskAssignments!");
    }
    
    const assignments = await fetchAllTaskAssignments();
    setEvents(mapAssignmentsToEvents(assignments));
    const allInclInactive = await getAllTasksInclInactive();
    setAllTasksInclInactive(allInclInactive);
  };

  /**
   * Handler for assigning a task to staff (e.g. via drag & drop).
   * Optionally deactivates the task after assignment.
   */
  const handleTaskAssigned = async (assignment, task, removeAfterAssign) => {
    if (removeAfterAssign && task) {
      
      const assignments = await fetchAllTaskAssignments();
      const assignedStaff = assignments.filter(a => a.taskId === task.id);
      
      const qualOrder = ["NONE", "JUNIOR", "SENIOR", "MANAGER"];
      const requiredIdx = qualOrder.indexOf(task.qualificationLevel);
      
      let qualified = false;
      for (const a of assignedStaff) {
        const staffObj = staff.find(s => s.id === a.staffId);
        if (staffObj) {
          const staffIdx = qualOrder.indexOf(staffObj.staffQualificationLevel);
          if (staffIdx >= requiredIdx) {
            qualified = true;
            break;
          }
        }
      }
      if (!qualified) {
        window.alert("Task can't be removed, minQualification of the task is not assigned to an employee.");
      } else {
        await deactivateTask(task);
      }
    }
    
    const assignments = await fetchAllTaskAssignments();
    setEvents(mapAssignmentsToEvents(assignments));
    const allInclInactive = await getAllTasksInclInactive();
    setAllTasksInclInactive(allInclInactive);
  };

  return (
    <>
      <Navbar />
      <main className="planner-main">
        <div className="planner-content">
          <aside className="planner-sidebar">
            <StaffList
              staff={staff}
              onSelect={setSelectedStaff}
              selected={selectedStaff}
            />
            <button
              style={{ marginBottom: 12, padding: '8px 16px', background: '#212529', color: 'white', border: 'none', borderRadius: 4, cursor: 'pointer', fontWeight: 500 }}
              onClick={async () => {

                const toActivate = allTasksInclInactive.filter(t => t.defaultTask && !t.active);

                await Promise.all(toActivate.map(async t => {
                  try {
                    await updateTaskActiveStatus(t.id, true);
                  } catch (err) {
                    if (err.response) {
                      console.error('Update failed for task', t.id, 'Response:', err.response.data);
                    } else {
                      console.error('Update failed for task', t.id, err);
                    }
                  }
                }));

                getTasks().then(setTasks);
                getAllTasksInclInactive().then(setAllTasksInclInactive);
              }}
            >
              Activate Default Tasks
            </button>
            <TaskList
              tasks={tasks}
              selectedStaff={selectedStaff}
              onUpdateTask={handleTaskCreated}
            />
          </aside>
          <section className="planner-calendar-wrap">
            <div className="calendar-view-controls" style={{ marginBottom: 12, display: 'flex', gap: 8 }}>
              <button onClick={() => setCalendarView("timeGridDay")} className={calendarView === "timeGridDay" ? "active" : ""}>Day</button>
              <button onClick={() => setCalendarView("timeGridWeek")} className={calendarView === "timeGridWeek" ? "active" : ""}>Week</button>
              <button onClick={() => setCalendarView("dayGridMonth")} className={calendarView === "dayGridMonth" ? "active" : ""}>Month</button>
            </div>
            <Calendar
              events={events}
              onEventDrop={handleEventChange}
              onEventResize={handleEventChange}
              tasks={tasks}
              selectedStaff={selectedStaff}
              onTaskAssigned={handleTaskAssigned}
              staffColorMap={staffColorMap}
              calendarView={calendarView}
              onViewDatesChange={setCalendarDates}
            />
            
            <div style={{ display: 'flex', gap: 32 }}>
              <div style={{ display: 'flex', gap: 32, width: '100%' }}>
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ background: '#fff', borderRadius: 8, boxShadow: '0 2px 8px rgba(25,118,210,0.07)', padding: 16, marginBottom: 8 }}>
                    <AssignmentOverviewPerTask
                      key={`${calendarDates.start ? calendarDates.start.toISOString() : ""}${calendarDates.end ? calendarDates.end.toISOString() : ""}`}
                      events={events}
                      tasks={allTasksInclInactive}
                      staff={staff}
                      calendarDates={calendarDates}
                    />
                  </div>
                </div>
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ background: '#fff', borderRadius: 8, boxShadow: '0 2px 8px rgba(25,118,210,0.07)', padding: 16, marginBottom: 8 }}>
                    <AssignmentOverviewPerStaff
                      key={'staff-' + (calendarDates.start ? calendarDates.start.toISOString() : 'null') + (calendarDates.end ? calendarDates.end.toISOString() : 'null')}
                      events={events}
                      tasks={allTasksInclInactive}
                      staff={staff}
                      calendarDates={calendarDates}
                    />
                  </div>
                </div>
              </div>
            </div>

          </section>
        </div>
      </main>
    </>
  );
}

