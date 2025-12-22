import React, { useState, useEffect } from "react";

export default function TaskList({ tasks, selectedStaff, onAddTask }) {
  const [newTaskName, setNewTaskName] = useState("");
  const [newTaskDescription, setNewTaskDescription] = useState("");
  const [qualificationLevels, setQualificationLevels] = useState([]);
  const [newTaskQualification, setNewTaskQualification] = useState("");
  const [removeAfterAssign, setRemoveAfterAssign] = useState(false);
  const [taskStaffMap, setTaskStaffMap] = useState({}); // taskId -> staffName

  useEffect(() => {
    const token =
      localStorage.getItem("token") ||
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pblVzZXIiLCJyb2xlcyI6WyJTWVNURU1fQURNSU4iXSwiaWF0IjoxNzY2NDEzNDkyLCJleHAiOjE3NjY0OTk4OTJ9.JLoBp9EjYkNyP2lpTDVow7G5epAHWY_0KL1s7GDS9JI";
    fetch("http://localhost:8080/api/qualification-levels", {
      headers: {
        Authorization: token ? `Bearer ${token}` : undefined,
        "Content-Type": "application/json",
      },
    })
      .then((res) => res.json())
      .then((levels) => {
        setQualificationLevels(levels);
        if (levels.length > 0) setNewTaskQualification(levels[0]);
      });
  }, []);

  const handleAddTask = (e) => {
    e.preventDefault();
    if (!newTaskName.trim() || !newTaskQualification) return;
    onAddTask({
      name: newTaskName,
      description: newTaskDescription,
      qualificationLevel: newTaskQualification,
    });
    setNewTaskName("");
    setNewTaskDescription("");
    setNewTaskQualification(qualificationLevels[0] || "");
  };

  // Task anklicken: Wenn Mitarbeiter ausgewählt, Task markieren und Staff zuordnen
  const handleTaskClick = (task) => {
    if (selectedStaff) {
      setTaskStaffMap((prev) => ({
        ...prev,
        [task.id]:
          typeof selectedStaff.name === "string"
            ? selectedStaff.name
            : selectedStaff.name.firstName + " " + selectedStaff.name.lastName,
      }));
    }
  };

  const isDummyStaff = selectedStaff && typeof selectedStaff.id === "string" && selectedStaff.id.startsWith("dummy");

  return (
    <div>
      <h3>Tasks</h3>
      <ul className="task-draggable-list" style={{ listStyle: "none", padding: 0 }}>
        {tasks.map((task, idx) => {
          // Hole die zuletzt zugeordnete staffId explizit aus taskStaffMap (nicht selectedStaff!)
          const staffName = taskStaffMap[task.id];
          // Speichere staffId direkt im taskStaffMap, wenn Task angeklickt wird:
          // taskStaffMap[task.id] = { name: ..., id: ... }
          // Daher: taskStaffMap[task.id]?.id und taskStaffMap[task.id]?.name
          const staffObj = taskStaffMap[task.id];
          const staffId = staffObj && staffObj.id ? staffObj.id : "";
          const staffDisplayName = staffObj && staffObj.name ? staffObj.name : "";

          return (
            <li
              key={task.id ?? `task-${idx}`}
              className="fc-draggable-task"
              data-task-id={task.id}
              data-title={staffDisplayName ? `${staffDisplayName}: ${task.name}` : task.name}
              data-staff-id={staffId}
              data-staff-name={staffDisplayName}
              style={{
                padding: "8px",
                margin: "4px 0",
                borderRadius: "4px",
                background: "#f0f7ff",
                border: "1px solid #ddd",
                cursor: "pointer",
                opacity: isDummyStaff ? 0.5 : 1,
                pointerEvents: isDummyStaff ? "none" : "auto",
              }}
              draggable={!isDummyStaff}
              onClick={() => {
                if (selectedStaff) {
                  setTaskStaffMap((prev) => ({
                    ...prev,
                    [task.id]: {
                      id: selectedStaff.id,
                      name:
                        typeof selectedStaff.name === "string"
                          ? selectedStaff.name
                          : selectedStaff.name.firstName + " " + selectedStaff.name.lastName,
                    },
                  }));
                }
              }}
            >
              {staffDisplayName && (
                <span style={{ color: "#1976d2", fontWeight: "bold" }}>
                  {staffDisplayName}:{" "}
                </span>
              )}
              <strong>{task.name}</strong>
              {task.qualificationLevel ? ` (${task.qualificationLevel})` : ""}
              {task.description ? ` — ${task.description}` : ""}
            </li>
          );
        })}
      </ul>
      {isDummyStaff && (
        <div style={{ color: "red", marginTop: 8 }}>
          Bitte echten Mitarbeiter auswählen, um Tasks zuzuordnen!
        </div>
      )}
      {qualificationLevels.length > 0 && (
        <form onSubmit={handleAddTask} style={{ marginTop: "12px" }}>
          <input
            type="text"
            placeholder="Task name"
            value={newTaskName}
            onChange={(e) => setNewTaskName(e.target.value)}
            style={{ marginRight: "8px", padding: "4px" }}
          />
          <input
            type="text"
            placeholder="Description"
            value={newTaskDescription}
            onChange={(e) => setNewTaskDescription(e.target.value)}
            style={{ marginRight: "8px", padding: "4px" }}
          />
          <select
            value={newTaskQualification}
            onChange={(e) => setNewTaskQualification(e.target.value)}
            style={{ marginRight: "8px", padding: "4px" }}
          >
            {qualificationLevels.map((level) => (
              <option key={level} value={level}>
                {level}
              </option>
            ))}
          </select>
          <button type="submit">Add Task</button>
        </form>
      )}
      <div style={{ marginTop: "10px" }}>
        <input
          type="checkbox"
          id="remove-after-assign"
          checked={removeAfterAssign}
          onChange={() => setRemoveAfterAssign(!removeAfterAssign)}
        />
        <label htmlFor="remove-after-assign" style={{ marginLeft: "6px" }}>
          Entferne Task nach Zuteilung in den Kalender
        </label>
      </div>
    </div>
  );
}
