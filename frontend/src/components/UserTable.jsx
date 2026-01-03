import {useMemo, useState} from "react";

export default function UsersTable({
                                       users,
                                       onToggleActive,
                                       onEditRoles,
                                       canEditUserRoles,
                                       canEditStaffQualification,
                                       onEditStaffQualification,
                                       onEditFte
                                   }) {
    // Add sorting state
    const [sortConfig, setSortConfig] = useState({
        key: null,
        direction: "asc",
    });

    const sortedUsers = useMemo(() => {
        if (!sortConfig.key) return users;

        return [...users].sort((a, b) => {
            const aVal = a[sortConfig.key] ?? "";
            const bVal = b[sortConfig.key] ?? "";

            if (typeof aVal === "string") {
                return sortConfig.direction === "asc"
                    ? aVal.localeCompare(bVal)
                    : bVal.localeCompare(aVal);
            }

            return sortConfig.direction === "asc"
                ? aVal - bVal
                : bVal - aVal;
        });
    }, [users, sortConfig]);

    /**
     * Sort a given column with its key
     * @param {string} key Value to sort for
     * @return {string, string}
     */
    function requestSort(key) {
        setSortConfig(prev => {
            if (prev.key === key) {
                return {
                    key,
                    direction: prev.direction === "asc" ? "desc" : "asc",
                };
            }
            return { key, direction: "asc" };
        });
    }

    return (
        <table className="table table-striped mt-3">
            <thead>
            <tr>
                <th onClick={() => requestSort("username")} style={{ cursor: "pointer" }}>
                    Username {sortConfig.key === "username" ? (sortConfig.direction === "asc" ? "▲" : "▼") : ""}
                </th>

                <th onClick={() => requestSort("lastName")} style={{ cursor: "pointer" }}>
                    Name {sortConfig.key === "lastName" ? (sortConfig.direction === "asc" ? "▲" : "▼") : ""}
                </th>

                <th onClick={() => requestSort("staffQualificationLevel")} style={{ cursor: "pointer" }}>
                    Qualification {sortConfig.key === "staffQualificationLevel" ? (sortConfig.direction === "asc" ? "▲" : "▼") : ""}
                </th>

                <th>FTE</th>

                {canEditUserRoles && (
                    <th onClick={() => requestSort("roles")} style={{ cursor: "pointer" }}>
                        User Roles
                    </th>
                )}

                <th onClick={() => requestSort("enabled")} style={{ cursor: "pointer" }}>
                    Status {sortConfig.key === "enabled" ? (sortConfig.direction === "asc" ? "▲" : "▼") : ""}
                </th>
                <th>Actions</th>
            </tr>
            </thead>

            <tbody>
            {sortedUsers.map(user => (
                <tr key={user.id}>
                    <td>{user.username}</td>
                    <td>{user.firstName} {user.lastName}</td>
                    <td>{user.staffQualificationLevel}</td>
                    <td>{user.fte}</td>
                    {/* USER ROLES – ADMINS ONLY */}
                    {canEditUserRoles && (
                        <td>{user.roles.join(", ")}</td>
                    )}
                    <td>{user.active ? "Active" : "Inactive"}</td>
                    <td>
                        <div className="dropdown">
                            <button
                                className="btn btn-sm btn-outline-primary dropdown-toggle"
                                data-bs-toggle="dropdown"
                            >
                                Actions
                            </button>

                            <ul className="dropdown-menu">
                                <li>
                                    <button
                                        className="dropdown-item"
                                        onClick={() => onToggleActive(user)}
                                    >
                                        {user.active
                                            ? "Deactivate user"
                                            : "Activate user"}
                                    </button>
                                </li>
                                {/*Manager permission*/}
                                {canEditStaffQualification && (
                                    <>
                                        <li>
                                            <button
                                                className="dropdown-item"
                                                data-bs-toggle="modal"
                                                data-bs-target="#staffRoleModal"
                                                onClick={() => onEditStaffQualification(user)}
                                            >
                                                Update staff qualification
                                            </button>
                                        </li>

                                        <li>
                                            <button
                                                className="dropdown-item"
                                                data-bs-toggle="modal"
                                                data-bs-target="#fteModal"
                                                onClick={() => onEditFte(user)}
                                            >
                                                Change FTE
                                            </button>
                                        </li>
                                    </>
                                )}

                                {/*Admin Permission*/}
                                {canEditUserRoles && (
                                    <li>
                                        <button
                                            className="dropdown-item"
                                            data-bs-toggle="modal"
                                            data-bs-target="#userRoleModal"
                                                onClick={() => onEditRoles(user)}
                                        >
                                            Update roles
                                        </button>
                                    </li>
                                )}
                            </ul>
                        </div>
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}