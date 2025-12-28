import Navbar from "../components/Navbar";
import TaskAssignmentList from "../components/TaskAssignmentList";

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

            <div style={{padding: "16px"}}>
                <div style={{ maxWidth: "900px", margin: "0 auto" }}>
                    <div
                        style={{
                            border: "1px solid #ddd",
                            borderRadius: "8px",
                            padding: "16px",
                            background: "#fff",
                        }}
                    >
                        <h4 style={{ marginTop: 0, marginBottom: "12px" }}>
                            Assigned Tasks
                        </h4>

                        <TaskAssignmentList />
                    </div>
                </div>
            </div>
        </>
    );
}
