import { Link } from "react-router-dom";

export default function RegistrationSuccessPage() {
    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
            <div className="card shadow p-4 text-center" style={{ maxWidth: "420px" }}>
                <h2 className="mb-3">Registration successful 🎉</h2>
                <p className="text-muted mb-4">
                    Your account has been created. You can now log in.
                </p>

                <Link to="/login" className="btn btn-primary w-100">
                    Go to Login
                </Link>
            </div>
        </div>
    );
}