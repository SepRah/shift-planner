import { FaEye, FaEyeSlash } from "react-icons/fa";
import { useState } from "react";

export default function PasswordInput({
                                          id,
                                          label,
                                          value,
                                          onChange,
                                          placeholder,
                                          isInvalid,
                                          errorMessage,
                                          autoComplete = "new-password"
                                      }) {
    const [show, setShow] = useState(false);

    return (
        <div className="mb-3">
            <label htmlFor={id} className="form-label">
                {label}
            </label>

            <div className="input-group has-validation">
                <input
                    id={id}
                    type={show ? "text" : "password"}
                    className={`form-control ${isInvalid ? "is-invalid" : ""}`}
                    placeholder={placeholder}
                    value={value}
                    onChange={onChange}
                    autoComplete={autoComplete}
                    required
                />

                <button
                    type="button"
                    className="btn btn-outline-secondary"
                    onClick={() => setShow(prev => !prev)}
                    tabIndex={-1}
                >
                    {show ? <FaEyeSlash /> : <FaEye />}
                </button>
            </div>

            {isInvalid && (
                <div className="invalid-feedback d-block">
                    {errorMessage}
                </div>
            )}
        </div>
    );
}
