import { Navigate } from "react-router-dom";
import { getUserRoles } from "../auth/auth";

export default function AdminRoute({ children }) {
    const roles = getUserRoles();

    const isAdmin =
        roles.includes("ADMIN") || roles.includes("SYSTEM_ADMIN");

    if (!isAdmin) {
        return <Navigate to="/unauthorized" replace />;
    }

    return children;
}