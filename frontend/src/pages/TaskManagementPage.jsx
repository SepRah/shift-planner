
import React, { useEffect, useState } from "react";
import TaskList from "../components/TaskList";
import { createTask, updateTask, getAllTasksInclInactive } from "../api/taskApi";

export default function TaskManagementPage() {
    const [tasks, setTasks] = useState([]);

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
            alert("Error creating task");
        }
    };

    const handleToggleActive = async (task) => {
        if (!task || !task.id) return;
        try {
            await updateTask(task.id, { ...task, active: !task.active });
            reloadTasks();
        } catch (err) {
            alert("Status - Update failed");
        }
    };

    const defaultTasks = tasks.filter(t => t.defaultTask);
    const nonDefaultTasks = tasks.filter(t => !t.defaultTask);

    return (
        <div>
            <h2>Task management</h2>
            <p>Here you can create your default tasks or reactivate old ones.</p>

            <h3>Default tasks</h3>
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

            <h3>Further tasks</h3>
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