import React from "react";
import "./Toolbar.css"

function Toolbar() {
    

    return (
    <header className="header">
        <a href="/" className="logo">Logo</a>

            <nav className="navbar">
                <a href="#">Home</a>
                <a href="#">About</a>
                <a href="#">FAQ</a>
            </nav>
    </header>
    )
}

export default Toolbar;