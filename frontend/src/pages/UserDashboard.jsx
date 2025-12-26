import {useEffect, useState} from "react";
import Navbar from "../components/Navbar";
import {getMe} from "../api/userAccountApi.js";

export default function UserDashboard(){

    const [profile, setProfile] = useState(null);


    useEffect(() => {
        document.title = "Shiftplanner – User Dashboard";
        // Get the user data
        const loadUserData = async () => {
            try {
                const res = await getMe();
                setProfile(res);
            } catch (err) {
                console.error("Failed to load profile", err);
            }
        };

        loadUserData();
    }, []);

    console.log("Profile loaded:", profile);


    return (
        <>
            <Navbar />
            <div className="container mt-4">
                <h2>User Dashboard</h2>

            </div>
        </>
    )
}