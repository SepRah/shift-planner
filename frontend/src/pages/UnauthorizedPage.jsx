import { useLocation, useNavigate } from "react-router-dom";

export default function UnauthorizedPage() {
    // Get the Location where the user came from
    const location = useLocation();
    const navigate = useNavigate();

    const from = location.state?.from?.pathname || "/";

    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
            <div className="card bg-warning p-4" style={{ maxWidth: "400px", width: "100%" }}>
                <h2 className="text-center mb-3">Access Denied</h2>

                <p className="text-center text-muted mb-4">
                    You do not have the rights to access this page.
                </p>

                <button
                    className="btn btn-dark w-100"
                    onClick={() => navigate(from)}
                >
                    Go back
                </button>
            </div>
        </div>
    );
}