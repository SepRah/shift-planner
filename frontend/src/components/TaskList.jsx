
import React, { useState, useEffect } from "react";
import "../styles/SidebarLists.css";
import { createTask, getQualificationLevels } from "../api/taskApi";

/**
 * TaskList component displays a scrollable list of tasks and a form to add new tasks.
 * Allows assignment of staff to tasks and removal of tasks.
 */
export default function TaskList({ tasks, selectedStaff, onUpdateTask, onRemoveTask }) {
  const taskListStyle = {
    maxHeight: '340px',
    overflowY: 'auto',
    paddingRight: 4
  };

  // Remove a task (delegates qualification check to parent)
  const handleRemoveTask = (task) => {
    if (onRemoveTask) {
      onRemoveTask(task);
    }
  };

  const [newTaskName, setNewTaskName] = useState("");
  const [newTaskDescription, setNewTaskDescription] = useState("");
  const [qualificationLevels, setQualificationLevels] = useState([]);
  const [newTaskQualification, setNewTaskQualification] = useState("");
  const [removeAfterAssign, setRemoveAfterAssign] = useState(() => {
    // Load value from localStorage for cross-component access
    const stored = localStorage.getItem("removeAfterAssign");
    return stored === null ? false : stored === "true";
  });
  // Maps taskId to assigned staff info
  const [taskStaffMap, setTaskStaffMap] = useState({});

  useEffect(() => {
    localStorage.setItem("removeAfterAssign", removeAfterAssign);
    getQualificationLevels().then((levels) => {
      setQualificationLevels(levels);
      if (levels.length > 0) setNewTaskQualification(levels[0]);
    });
  }, []);

  // Add a new task
  const handleAddTask = async (e) => {
    e.preventDefault();
    if (!newTaskName.trim() || !newTaskQualification) return;
    try {
      await createTask({
        name: newTaskName,
        description: newTaskDescription,
        qualificationLevel: newTaskQualification,
      });
      if (onUpdateTask) {
        onUpdateTask();
      }
      setNewTaskName("");
      setNewTaskDescription("");
      setNewTaskQualification(qualificationLevels[0] || "");
    } catch (err) {
      alert("Error adding task.");
    }
  };

  // Assign staff to a task when clicked
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

  return (
    <>
      <div className="task-list" style={taskListStyle}>
        <h3>Tasks</h3>
        <ul className="task-draggable-list">
          {tasks.filter((task) => task.active !== false).map((task, idx) => {
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
                draggable={true}
                onClick={() => handleTaskClick(task)}
                style={{ padding: 0 }}
              >
                <div style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '10px 12px 0 12px' }}>
                  <strong>{task.name}</strong>
                  {task.qualificationLevel && (
                    <span className="task-qual" style={{ color: '#5a8d6dff' }}>{task.qualificationLevel}</span>
                  )}
                </div>
                <hr style={{ margin: '6px 0 6px 0', border: 0, borderTop: '1px solid #e3e8ee' }} />
                {task.description && (
                  <div className="task-desc" style={{ display: 'block', padding: '0 12px 10px 12px', whiteSpace: 'pre-line', wordBreak: 'break-word', width: '100%' }}>
                    {task.description}
                  </div>
                )}
                {staffDisplayName && (
                  <>
                    <hr style={{ margin: '0 0 6px 0', border: 0, borderTop: '1px solid #e3e8ee' }} />
                    <div className="assigned-staff" style={{ fontWeight: 500, color: '#388e3c', padding: '0 12px 10px 12px' }}>{staffDisplayName}</div>
                  </>
                )}
              </li>
            );
          })}
        </ul>
      </div>

      {qualificationLevels.length > 0 && (
        <form onSubmit={handleAddTask} className="add-task-form add-task-form-block" style={{ marginTop: 12 }}>
          <label className="add-task-label" htmlFor="add-task-title">Add task</label>
          <input
            id="add-task-title"
            type="text"
            placeholder="Task titel"
            value={newTaskName}
            onChange={(e) => setNewTaskName(e.target.value)}
            className="add-task-input add-task-input-block"
            required
            maxLength={60}
            style={{ minHeight: '32px', maxHeight: '38px', lineHeight: '1.2', marginBottom: 0 }}
          />
          <textarea
            id="add-task-desc"
            placeholder="Description"
            value={newTaskDescription}
            onChange={(e) => setNewTaskDescription(e.target.value)}
            className="add-task-textarea"
            rows={2}
            maxLength={120}
            style={{ minHeight: '32px', maxHeight: '48px', resize: 'vertical', fontSize: '1em', marginBottom: 0 }}
          />
          <div className="add-task-row add-task-row-bottom">
            <select
              value={newTaskQualification}
              onChange={(e) => setNewTaskQualification(e.target.value)}
              className="add-task-select"
              required
              style={{ color: '#5a8d6dff' }}
            >
              {qualificationLevels.map((level) => (
                <option key={level} value={level} style={{ color: '#5a8d6dff' }}>
                  {level}
                </option>
              ))}
            </select>
            <button type="submit" className="add-task-btn" style={{ height: '36px', minHeight: 0, fontSize: '0.93em', padding: '0 14px', whiteSpace: 'nowrap' }}>Add task</button>
          </div>
        </form>
      )}
      <div style={{ marginTop: "10px" }}>
        <input
          type="checkbox"
          id="remove-after-assign"
          checked={removeAfterAssign}
          onChange={() => {
            setRemoveAfterAssign((prev) => {
              localStorage.setItem("removeAfterAssign", !prev);
              return !prev;
            });
          }}
        />
        <label htmlFor="remove-after-assign" style={{ marginLeft: "6px" }}>
          Remove task after assignment
        </label>
      </div>
    </>
  );
}
