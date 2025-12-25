import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import RegistrationSuccessPage from "./pages/RegistrationSuccesspage.jsx";
import PlannerPage from "./pages/PlannerPage.jsx";
import HomePage from "./pages/HomePage.jsx";
import AdminRoute from "./routes/AdminRoute.jsx";
import AdminDashboard from "./pages/AdminDashboard.jsx";
import UnauthorizedPage from "./pages/UnauthorizedPage.jsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                {/* Default route -> go to /login */}
                <Route path="/" element={<Navigate to="/login" />} />

                {/* Auth routes */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/register/success" element={<RegistrationSuccessPage />} />

                {/* Homepage route */}
                <Route path="/home" element={<HomePage />} />

                {/* Planner route */}
                <Route path="/planner" element={<PlannerPage />} />

                {/* Admin route */}
                <Route
                    path="/admin"
                    element={
                        <AdminRoute>
                            <AdminDashboard />
                        </AdminRoute>
                    }
                />

                {/* Unauthorized */}
                <Route path="/unauthorized" element={<UnauthorizedPage />} />
                {/* Optional: 404 page */}
                <Route path="*" element={<div>Page not found</div>} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;