import { useEffect, useState } from "react";
import {
    fetchAllUsers,
    activateUser,
    deactivateUser,
    updateUserRoles
} from "../api/adminApi";
import Navbar from "../components/Navbar";

export default function AdminDashboard() {
    const [users, setUsers] = useState([]);

    const [selectedUser, setSelectedUser] = useState(null);
    const [selectedRoles, setSelectedRoles] = useState([]);

    /**
     * Loads all users for the admin dashboard.
     *
     * Fetches the user list from the admin API and updates
     * the local component state used for rendering.
     */
    async function loadUsers() {
        const data = await fetchAllUsers();
        setUsers(data);
    }

    useEffect(() => {
        document.title = "Shiftplanner – Admin Dashboard";
        loadUsers();
    }, []);

    /**
     * Sets a user active or deactivated
     */
    async function toggleActive(user) {
        if (user.active) {
            await deactivateUser(user.id);
        } else {
            await activateUser(user.id);
        }
        loadUsers();
    }

    /**
     * Toggles the roles
     */
    function toggleRole(role) {
        setSelectedRoles(prev =>
            prev.includes(role)
                ? prev.filter(r => r !== role)
                : [...prev, role]
        );
    }

    /**
     * Saves the roles
     */
    async function saveRoles() {
        await updateUserRoles(selectedUser.id, selectedRoles);
        loadUsers();
    }

    return (
        <>
        <Navbar />

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
                            <div className="dropdown">
                                <button
                                    className="btn btn-sm btn-outline-primary dropdown-toggle"
                                    type="button"
                                    data-bs-toggle="dropdown"
                                >
                                    Actions
                                </button>

                                <ul className="dropdown-menu">
                                    <li>
                                        <button
                                            className="dropdown-item"
                                            onClick={() => toggleActive(user)}
                                        >
                                            {user.active ? "Deactivate user" : "Activate user"}
                                        </button>
                                    </li>

                                    <li>
                                        <button
                                            className="dropdown-item"
                                            data-bs-toggle="modal"
                                            data-bs-target="#roleModal"
                                            onClick={() => {
                                                setSelectedUser(user);
                                                setSelectedRoles(user.roles);
                                            }}
                                        >
                                            Update roles
                                        </button>
                                    </li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>

            {/*The Role Modal*/}
            <div className="modal fade" id="roleModal" tabIndex="-1" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">

                        <div className="modal-header">
                            <h5 className="modal-title">
                                Update roles for {selectedUser?.username}
                            </h5>
                            <button
                                type="button"
                                className="btn-close"
                                data-bs-dismiss="modal"
                            />
                        </div>

                        <div className="modal-body">
                            {["USER", "ADMIN", "SYSTEM_ADMIN"].map(role => (
                                <div className="form-check" key={role}>
                                    <input
                                        className="form-check-input"
                                        type="checkbox"
                                        checked={selectedRoles.includes(role)}
                                        onChange={() => toggleRole(role)}
                                    />
                                    <label className="form-check-label">
                                        {role}
                                    </label>
                                </div>
                            ))}
                        </div>

                        <div className="modal-footer">
                            <button
                                type="button"
                                className="btn btn-secondary"
                                data-bs-dismiss="modal"
                            >
                                Cancel
                            </button>

                            <button
                                className="btn btn-primary"
                                data-bs-dismiss="modal"
                                onClick={saveRoles}
                            >
                                Save changes
                            </button>
                        </div>

                    </div>
                </div>
            </div>
        </>
    );
}
