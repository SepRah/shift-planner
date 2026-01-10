import Navbar from "../components/Navbar";
import MyTaskAssignmentsCalender from  "../components/MyTaskAssignmentsCalender.jsx"
import React, {useState} from "react";

export default function HomePage() {
    const [calendarView, setCalendarView] = useState("timeGridWeek");
    const [calendarDates, setCalendarDates] = useState({ start: null, end: null });


    return (
        <>
            <Navbar />

            <main style={{ padding: "1rem" }}>
                <h1>Welcome to ShiftPlanner</h1>
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
                        <div className="calendar-view-controls" style={{ marginBottom: 12, display: 'flex', gap: 8 }}>
                            <button onClick={() => setCalendarView("timeGridDay")} className={calendarView === "timeGridDay" ? "active" : ""}>Day</button>
                            <button onClick={() => setCalendarView("timeGridWeek")} className={calendarView === "timeGridWeek" ? "active" : ""}>Week</button>
                            <button onClick={() => setCalendarView("dayGridMonth")} className={calendarView === "dayGridMonth" ? "active" : ""}>Month</button>
                        </div>

                        <MyTaskAssignmentsCalender
                            calendarView={calendarView}
                            onViewDatesChange={setCalendarDates}
                        />
                    </div>
                </div>
            </div>
        </>
    );
}
