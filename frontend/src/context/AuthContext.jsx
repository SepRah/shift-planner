import { createContext, useContext, useEffect, useState } from "react";
import {jwtDecode} from "jwt-decode";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    /**
     * Logs the user in:
     * - saves JWT
     * - decodes roles
     * - updates context state
     */
    const login = (token) => {
        localStorage.setItem("token", token);

        const decoded = jwtDecode(token);
        // set the user
        setUser({
            username: decoded.sub,
            roles: decoded.roles || [],          // system roles
            staff: decoded.staff || []            // staff qualifications
        });
    };

    /**
     * Logs the user out:
     * - removes JWT
     * - clears context state
     */
    const logout = () => {
        localStorage.removeItem("token");
        setUser(null);
    };

    /**
     * Restore auth state on app reload
     */
    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token) {
            try {
                login(token);
            } catch (e) {
                console.error("Invalid token, logging out");
                logout();
            }
        }

        setLoading(false);
    }, []);

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
}

/**
 * Safe hook for accessing auth context
 */
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};
