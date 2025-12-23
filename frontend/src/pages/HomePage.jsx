import Navbar from "../components/Navbar";

export default function HomePage() {
    return (
        <>
            <Navbar />

            <main style={{ padding: "1rem" }}>
                <h1>Welcome to ShiftPlanner</h1>
                <p>
                    Manage staff members and assign tasks based on qualification levels.
                </p>
            </main>
        </>
    );
}
