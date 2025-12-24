import { useEffect, useState } from "react";
import {
    fetchAdminUsers,
    activateUser,
    deactivateUser
} from "../api/adminApi";

export default function AdminDashboard() {
    const [users, setUsers] = useState([]);

    async function loadUsers() {
        const data = await fetchAdminUsers();
        setUsers(data);
    }

    useEffect(() => {
        loadUsers();
    }, []);

    async function toggleActive(user) {
        if (user.active) {
            await deactivateUser(user.id);
        } else {
            await activateUser(user.id);
        }
        loadUsers();
    }

    return (
        <div className="container mt-4">
            <h2>Admin Dashboard</h2>

            <table className="table table-striped mt-3">
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Name</th>
                    <th>Roles</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td>{user.username}</td>
                        <td>{user.firstName} {user.lastName}</td>
                        <td>{user.roles.join(", ")}</td>
                        <td>
                            {user.active ? "Active" : "Inactive"}
                        </td>
                        <td>
                            <button
                                className="btn btn-sm btn-outline-primary"
                                onClick={() => toggleActive(user)}
                            >
                                {user.active ? "Deactivate" : "Activate"}
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
