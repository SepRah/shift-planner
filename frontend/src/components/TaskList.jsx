import React, { useState, useEffect } from "react";

export default function TaskList({ tasks, selectedStaff, onAddTask }) {
  const [newTaskName, setNewTaskName] = useState("");
  const [newTaskDescription, setNewTaskDescription] = useState("");
  const [qualificationLevels, setQualificationLevels] = useState([]);
  const [newTaskQualification, setNewTaskQualification] = useState("");
  const [removeAfterAssign, setRemoveAfterAssign] = useState(false);
  const [taskStaffMap, setTaskStaffMap] = useState({}); // taskId -> {id, name}

  useEffect(() => {
    
    fetch("http://localhost:8080/api/qualification-levels", {
      headers: {
        Authorization: localStorage.getItem("token") ? `Bearer ${localStorage.getItem("token")}` : undefined,
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
      let staffName = "";
      if (selectedStaff.firstName && selectedStaff.lastName) {
        staffName = `${selectedStaff.firstName} ${selectedStaff.lastName}`;
      } else if (
        selectedStaff.name &&
        typeof selectedStaff.name === "object"
      ) {
        const first = selectedStaff.name.firstName || "";
        const last = selectedStaff.name.lastName || "";
        staffName = [first, last].filter(Boolean).join(" ");
      } else if (selectedStaff.name) {
        staffName = selectedStaff.name;
      }
      setTaskStaffMap((prev) => ({
        ...prev,
        [task.id]: {
          id: selectedStaff.id,
          name: staffName,
        },
      }));
    }
  };

  const isDummyStaff = selectedStaff && typeof selectedStaff.id === "string" && selectedStaff.id.startsWith("dummy");

  return (
    <div>
      <h3>Tasks</h3>
      <ul className="task-draggable-list" style={{ listStyle: "none", padding: 0 }}>
        {tasks.map((task, idx) => {
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
              }}
              draggable={true}
              onClick={() => handleTaskClick(task)}
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
