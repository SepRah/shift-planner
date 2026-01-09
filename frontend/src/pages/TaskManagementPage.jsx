
import React, { useEffect, useState } from "react";
import TaskList from "../components/TaskList";
import { createTask, updateTask, getAllTasksInclInactive } from "../api/taskApi";


export default function TaskManagementPage() {
    const [tasks, setTasks] = useState([]);

    // Load all tasks (active and inactive)
    const reloadTasks = async () => {
        try {
            const allTasks = await getAllTasksInclInactive();
            setTasks(allTasks);
        } catch {
            setTasks([]);
        }
    };

    useEffect(() => {
        reloadTasks();
    }, []);

    // Create a new default task
    const handleAddTask = async (taskData) => {
        try {
            const newTask = {
                name: taskData.name,
                description: taskData.description,
                qualificationLevel: taskData.qualificationLevel,
                defaultTask: true,
                active: true,
            };
            await createTask(newTask);
            reloadTasks();
        } catch (err) {
            alert("Fehler beim Erstellen des Tasks.");
        }
    };

    // Toggle active status for a task
    const handleToggleActive = async (task) => {
        if (!task || !task.id) return;
        try {
            await updateTask(task.id, { ...task, active: !task.active });
            reloadTasks();
        } catch (err) {
            alert("Status - Update fehlgeschlagen");
        }
    };

        const defaultTasks = tasks.filter(t => t.defaultTask);
        const nonDefaultTasks = tasks.filter(t => !t.defaultTask);

        return (
            <div>
                <h2>Task Management</h2>
                <p>Hier kannst du neue Default - Tasks erstellen.</p>

                <h3>Default Tasks</h3>
                        <TaskList
                            tasks={defaultTasks}
                            onAddTask={handleAddTask}
                            onToggleActive={handleToggleActive}
                            showActiveToggle={true}
                            showRemoveAfterAssign={false}
                            showOnlyActive={false}
                            defaultTask={true}
                        />

                <hr style={{ margin: '32px 0', border: 0, borderTop: '2px solid #e3e8ee' }} />

                <h3>Weitere Tasks</h3>
                        <TaskList
                            tasks={nonDefaultTasks}
                            onToggleActive={handleToggleActive}
                            showActiveToggle={true}
                            showRemoveAfterAssign={false}
                            showOnlyActive={false}
                            hideAddForm={true}
                        />
            </div>
        );
}