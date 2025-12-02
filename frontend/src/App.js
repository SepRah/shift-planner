import React, { useState } from "react";

function App() {
    const [message, setMessage] = useState("Hello world!");

    function handleClick() {
        setMessage("Button clicked!");
    }

    return (
        <div style={{ padding: "20px" }}>
            <h1>{message}</h1>
            <button onClick={handleClick}>
                Click me
            </button>
        </div>
    );
}

export default App;