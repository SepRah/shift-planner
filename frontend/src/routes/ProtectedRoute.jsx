import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";

export default function ProtectedRoute({ requiredRoles }) {
    const { user, loading } = useAuth();

    // Wait until auth state is restored
    if (loading) {
        return null;
    }
    // Not logged in
    if (!user) {
        return <Navigate to="/login" replace />;
    }

    // Role check
    if (
        requiredRoles &&
        !requiredRoles.some(role => user.roles.includes(role))
    ) {
        return <Navigate to="/unauthorized" replace />;
    }

    return <Outlet />;
}