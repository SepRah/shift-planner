export default function FTEModal(
    {
        modalId,
        user,
        title,
        value,
        onChange,
        onSave
    }
){
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
                        <label className="form-control-label">New FTE</label>
                        <input
                            type="number"
                            className="form-control"
                            placeholder="Set an FTE"
                            value={value ?? ""}
                            min="0"
                            max="1"
                            step="0.1"
                            onChange={(e) => onChange(Number(e.target.value))}
                            autoComplete="off"
                            required
                        />
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
                            disabled={value === null}
                        >
                            Save changes
                        </button>
                    </div>

                </div>
            </div>
        </div>
    )
}