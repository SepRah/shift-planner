export default function UsersTable({
                                       users,
                                       onToggleActive,
                                       onEditRoles,
                                       canEditUserRoles,
                                       canEditStaffQualification,
                                       onEditStaffQualification
                                   }) {
    return (
        <table className="table table-striped mt-3">
            <thead>
            <tr>
                <th>Username</th>
                <th>Name</th>
                {/* Staff qualification column */}
                <th>Qualification</th>
                {/* User roles column – admin only */}
                {canEditUserRoles && <th>User Roles</th>}
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>

            <tbody>
            {users.map(user => (
                <tr key={user.id}>
                    <td>{user.username}</td>
                    <td>{user.firstName} {user.lastName}</td>
                    <td>{user.staffQualificationLevel}</td>
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
                                    <li>
                                        <button
                                            className="dropdown-item"
                                            data-bs-toggle="modal"
                                            data-bs-target="#staffRoleModal"
                                            onClick={() => {onEditStaffQualification(user);
                                            }}
                                        >
                                            Update staff qualification
                                        </button>
                                    </li>
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