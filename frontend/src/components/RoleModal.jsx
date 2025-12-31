export default function RoleModal({
                                      modalId,
                                      user,
                                      title,
                                      availableRoles,
                                      selectedRoles,
                                      selectionType = "multiple", // "multiple" | "single"
                                      onToggleRole,
                                      onSelectRole,
                                      onSave
                                  }) {
    return (
        <div
            className="modal fade"
            id={modalId}
            tabIndex="-1"
            aria-hidden="true"
        >
            <div className="modal-dialog">
                <div className="modal-content">

                    <div className="modal-header">
                        <h5 className="modal-title">
                            {title}{user ? ` for ${user.username}` : ""}
                        </h5>
                        <button
                            type="button"
                            className="btn-close"
                            data-bs-dismiss="modal"
                        />
                    </div>

                    <div className="modal-body">
                        {user && availableRoles.map(role => (
                            <div className="form-check" key={role}>
                                <input
                                    className="form-check-input"
                                    type={selectionType === "multiple" ? "checkbox" : "radio"}
                                    name="roleSelection"
                                    checked={
                                        selectionType === "multiple"
                                            ? selectedRoles.includes(role)
                                            : selectedRoles[0] === role
                                    }
                                    onChange={() =>
                                        selectionType === "multiple"
                                            ? onToggleRole(role)
                                            : onSelectRole(role)
                                    }
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
                            type="button"
                            className="btn btn-primary"
                            data-bs-dismiss="modal"
                            onClick={onSave}
                        >
                            Save changes
                        </button>
                    </div>

                </div>
            </div>
        </div>
    );
}