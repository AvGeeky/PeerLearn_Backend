import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import {VotingHeader} from "./pages/VotingHeader";
import RegistrationPage from "./pages/RegistrationPage";
import {LoginPage} from "./pages/LoginPage";
import PersonalInfo from "./pages/PersonalInfo";
import VotingInstructions from "./pages/VotingInstructions";
import {AboutPage} from "./pages/AboutPage";
import {Features} from "./pages/Features";
import VoterList from "./pages/VoterList";
import VotingPanel from "./pages/VotingPanel";
import {VotingSteps} from "./pages/VotingSteps";
import VotingInstructionss from "./pages/VotingInstructionss.jsx";

function App() {
    return (
        <Router>
            <Routes>
                {/* Home page route */}
                <Route path="/" element={<VotingHeader />} />
                <Route path="/registration" element={<RegistrationPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/personal-info" element={<PersonalInfo />} />
                <Route path="/voting-instructions" element={<VotingInstructions />} />
                <Route path="/about" element={<AboutPage />} />
                <Route path="/features" element={<Features />} />
                <Route path="/voter-list" element={<VoterList />} />
                <Route path="/voting-panel" element={<VotingPanel />} />
                <Route path="/voting-steps" element={<VotingSteps />} />
                <Route path="/voting-instructionss" element={<VotingInstructionss/>}/>
            </Routes>
        </Router>
    );
}

export default App;
