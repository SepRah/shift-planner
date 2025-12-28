import React, { useEffect, useState } from "react";
import StaffList from "../components/StaffList";
import TaskList from "../components/TaskList";
import Calendar from "../components/Calendar";

export default function PlannerPage() {
  const [staff, setStaff] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [events, setEvents] = useState([]);
  const [selectedStaff, setSelectedStaff] = useState(null);

  // Load staff and tasks from API on mount
  useEffect(() => {
    const token = localStorage.getItem("token");

    // Staff
    fetch("http://localhost:8080/api/staffmembers", {
      headers: {
        Authorization: token ? `Bearer ${token}` : undefined,
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch staff members");
        const contentType = res.headers.get("content-type");
        if (!contentType || !contentType.includes("application/json")) {
          throw new Error("Response is not JSON");
        }
        return res.json();
      })
      .then((data) => setStaff(data))
      .catch((err) => {
        console.error("Error loading staff members:", err);
        setStaff([]);
      });

    // Tasks
    fetch("http://localhost:8080/api/tasks", {
      headers: {
        Authorization: token ? `Bearer ${token}` : undefined,
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch tasks");
        const contentType = res.headers.get("content-type");
        if (!contentType || !contentType.includes("application/json")) {
          throw new Error("Response is not JSON");
        }
        return res.json();
      })
      .then((data) => setTasks(data))
      .catch((err) => {
        console.error("Error loading tasks:", err);
        setTasks([]);
      });

    // TaskAssignments für den Kalender laden
    fetch("http://localhost:8080/api/task-assignments", {
      headers: {
        Authorization: token ? `Bearer ${token}` : undefined,
        "Content-Type": "application/json",
      },
    })
      .then((res) => res.json())
      .then((assignments) => {
        // Mappe Assignments zu FullCalendar-Events
        const mappedEvents = assignments.map((a) => ({
          id: a.id,
          title: `${a.staffName ? a.staffName + ": " : ""}${a.taskName}`,
          start: a.timeRange?.start,
          end: a.timeRange?.end,
          extendedProps: {
            assignmentId: a.id,
            staffId: a.staffId,
            taskId: a.taskId,
          },
        }));
        setEvents(mappedEvents);
      })
      .catch((err) => {
        console.error("Error loading assignments:", err);
        setEvents([]);
      });
  }, []);

  // Funktion zum Hinzufügen eines neuen Tasks
  const addTask = (taskData) => {
    fetch("http://localhost:8080/api/tasks", {
      method: "POST",
      headers: {
        Authorization: localStorage.getItem("token") ? `Bearer ${localStorage.getItem("token")}` : undefined,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(taskData),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to create task");
        return res.json();
      })
      .then((newTask) => setTasks((prev) => [...prev, newTask]))
      .catch((err) => {
        console.error("Error creating task:", err);
      });
  };

  // Optional: Funktion, um Task nach Zuordnung aus der Liste zu entfernen
  const removeTask = (taskId) => {
    setTasks((prev) => prev.filter((t) => t.id !== taskId));
  };

  // Handler für Drag & Drop/Resize
  const handleEventChange = (event) => {
    fetch(`http://localhost:8080/api/task-assignments/${event.id}`, {
      method: "PUT",
      headers: {
        Authorization: localStorage.getItem("token") ? `Bearer ${localStorage.getItem("token")}` : undefined,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        timeRange: {
          start: event.start?.toISOString(),
          end: event.end?.toISOString(),
        },
      }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to update assignment");
        return res.json();
      })
      .then((updated) => {
        // Optional: Events im State aktualisieren
      })
      .catch((err) => {
        console.error("Error updating assignment:", err);
      });
  };

  return (
    <div className="app">
      <div className="sidebar">
        <StaffList
          staff={staff}
          onSelect={setSelectedStaff}
          selected={selectedStaff}
        />
        <TaskList
          tasks={tasks}
          selectedStaff={selectedStaff}
          onAddTask={addTask}
          onRemoveTask={removeTask} // optional, je nach Verhalten
        />
        {/* AddTaskForm kann in TaskList integriert werden */}
      </div>
      <div className="calendar-wrap">
        <Calendar
          events={events}
          onEventDrop={handleEventChange}
          onEventResize={handleEventChange}
        />
      </div>
    </div>
  );
}
