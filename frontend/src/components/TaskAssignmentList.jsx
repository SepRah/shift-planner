import { useEffect, useState } from "react";
import {fetchTaskAssignmentsByStaffId} from "../api/taskApi.js";
import {getMe} from "../api/userAccountApi.js";

export default function TaskAssignmentList() {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);


    async function loadMyTaskAssignments() {
        setLoading(true);
        setError(null);
        try {
            const me = await getMe();
            const data = await fetchTaskAssignmentsByStaffId(me.id);
            setItems(Array.isArray(data) ? data : []);
        } catch (err) {
            const msg = err?.response?.data?.message ??
                err?.message ??
                "Unknown error while loading task assignments";
            setError(msg);
            setItems([]);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        document.title = "My Tasks";
        loadMyTaskAssignments();
    }, []);

    if (loading) return <p>Load Tasks…</p>;

    if (error) return <p style={{color: "crimson"}}>Error: {error}</p>;

    if (items.length === 0) {return <p>No Tasks existing.</p>;}

    return (
        <ul style={{listStyle: "none", padding: 0, margin: 0}}>
            {items.map((a) => (
                <li
                    key={a.id}
                    style={{
                        border: "1px solid #eee",
                        borderRadius: "6px",
                        padding: "10px 12px",
                        marginBottom: "8px",
                    }}
                >
                    <div style={{display: "flex", justifyContent: "space-between", gap: "12px"}}>
                        <div>
                            <strong>{a.taskName ?? "Task"}</strong>
                            <div style={{fontSize: "0.9em", color: "#666", marginTop: "4px"}}>
                                {a.taskDescription ?? ""}
                            </div>
                        </div>

                        <div>
                            <div style={{fontSize: "0.9em", color: "#666", marginTop: "4px"}}>
                                {a.completed && "Completed"}
                            </div>
                        </div>

                        <div style={{textAlign: "right", fontSize: "0.9em", color: "#444"}}>
                            <div> start {formatDateTime(a.timeRange.start)}</div>
                            <div> end  {formatDateTime(a.timeRange.end)}</div>
                        </div>
                    </div>
                </li>
            ))}
        </ul>
    );

    function formatDateTime(value) {
        if (!value) return "-";
        // Wenn Backend ISO-Strings liefert (z.B. "2025-12-24T10:00:00"):
        const d = new Date(value);
        if (Number.isNaN(d.getTime())) return String(value);
        return d.toLocaleString();
    }
}
