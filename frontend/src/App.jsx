import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import RegistrationSuccessPage from "./pages/RegistrationSuccesspage.jsx";

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

                {/* Optional: 404 page */}
                <Route path="*" element={<div>Page not found</div>} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;