import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import RegistrationSuccessPage from "./pages/RegistrationSuccesspage.jsx";
import PlannerPage from "./pages/PlannerPage.jsx";
import HomePage from "./pages/HomePage.jsx";
import UserDashboard from "./pages/UserDashboard.jsx";
import UnauthorizedPage from "./pages/UnauthorizedPage.jsx";
import ProtectedRoute from "./routes/ProtectedRoute.jsx";
import ManagementLayout from "./routes/ManagementLayout.jsx";
import ManagementPage from "./pages/ManagementPage.jsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                {/* Default route -> go to /login */}
                <Route path="/" element={<Navigate to="/login" />} />

                {/* Public routes */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/register/success" element={<RegistrationSuccessPage />} />

                {/* Authenticated */}
                <Route element={<ProtectedRoute />}>
                    <Route path="/account" element={<UserDashboard />} />
                </Route>
                {/* Admin route */}
                <Route
                    element={
                        <ProtectedRoute
                            requiredRoles={["ADMIN", "SYSTEM_ADMIN"]}
                            requiredStaff={["MANAGER", "SENIOR"]}
                        />
                    }
                >
                    <Route path="/management" element={<ManagementLayout />}>
                        <Route index element={<ManagementPage />} />
                    </Route>
                </Route>

                {/* Homepage route */}
                <Route path="/home" element={<HomePage />} />

                {/* Planner route */}
                <Route path="/planner" element={<PlannerPage />} />

                {/* Unauthorized */}
                <Route path="/unauthorized" element={<UnauthorizedPage />} />
                {/* Optional: 404 page */}
                <Route path="*" element={<div>Page not found</div>} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;