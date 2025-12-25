import { useNavigate } from "react-router-dom";
import {useEffect} from "react";

export default function UnauthorizedPage() {

    // Adjust page title
    useEffect(() => {
        document.title = "Unauthorized";
    })
    // Get the Location where the user came from
    const navigate = useNavigate();

    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
            <div className="card bg-warning p-4" style={{ maxWidth: "400px", width: "100%" }}>
                <h2 className="text-center mb-3">Access Denied</h2>

                <p className="text-center text-muted mb-4">
                    You do not have the rights to access this page.
                </p>

                <button
                    className="btn btn-dark w-100"
                    onClick={() => navigate(-1)}
                >
                    Go back
                </button>
            </div>
        </div>
    );
}