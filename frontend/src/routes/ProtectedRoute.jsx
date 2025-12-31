import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";

export default function ProtectedRoute({
   requiredRoles = [],
   requiredStaff = []
}) {
    const { user, loading } = useAuth();

    // Wait until auth state is restored
    if (loading) {
        return null;
    }
    // Not logged in
    if (!user) {
        return <Navigate to="/login" replace />;
    }

    const hasSystemRole =
        requiredRoles.length === 0 ||
        requiredRoles.some(r => user.roles.includes(r));

    const hasStaffRole =
        requiredStaff.length === 0 ||
        requiredStaff.some(r => user.staff?.includes(r))

    // Role check
    if (!hasSystemRole && !hasStaffRole) {
        return <Navigate to="/unauthorized" replace />;
    }

    return <Outlet />;
}