import * as React from "react";
import { useNavigate } from "react-router-dom";

export function VotingInstructionss() {
    const navigate = useNavigate();
    const votingSteps = [
        {
            title: "Vote Freely",
            content: [
                "Make your voting decision independently.",
                "Do not let anyone pressure or intimidate you.",
                "If you feel threatened, please contact the provided helpline numbers."
            ]
        },
        {
            title: "Prepare for Voting",
            content: [
                "Have your Unique HASH-ID ready.",
                "Read through manifestos thoroughly and select your candidate carefully."
            ]
        },
        {
            title: "Voting Guidelines",
            content: [
                "You are allowed only one vote per election.",
                "You may choose only one candidate per vote.",
                "Candidate information is available on the voting page for your reference."
            ]
        },
        {
            title: "Selecting Your Candidate",
            content: [
                "Carefully select your desired candidate by confirming their name and symbol displayed on the screen."
            ]
        },
        {
            title: "Submitting Your Vote",
            content: [
                "After selecting your candidate, ensure you submit your vote to finalize the process."
            ]
        },
        {
            title: "Results Announcement",
            content: [
                "Election results will be announced automatically after the election concludes."
            ]
        },
        {
            title: "Vote Verification",
            content: [
                "You will have the opportunity to verify your vote status after you finish voting."
            ]
        }
    ];

    const handleCancel = () => {
        navigate("/");
    };

    return (
        <main className="min-h-screen w-screen flex items-center justify-center bg-gradient-to-b from-[#FF4E6E] via-[#DA5F9C] to-[#2E33D1] overflow-hidden px-4 py-8">
            <div className="w-full max-w-4xl bg-white/10 backdrop-blur-md rounded-xl p-8 shadow-2xl text-white">
                <header className="text-xl font-normal mb-6 text-center">
                    This election is being conducted by the election commission of SSN.
                </header>

                <article className="border-2 border-blue-400 rounded-lg p-8 bg-blue-900/40 mb-8">
                    <h1 className="text-3xl font-bold mb-6 text-center">
                        Voting Instructions for College Election
                    </h1>
                    <p className="mb-8 text-center">
                        As a member of the college community, you are eligible to vote. Please follow these steps to cast your vote:
                    </p>

                    <div className="space-y-8">
                        {votingSteps.map((step, index) => (
                            <section key={index} className="bg-white/5 backdrop-blur-sm rounded-lg p-6">
                                <h2 className="text-xl font-semibold mb-3">
                                    {index + 1}. {step.title}
                                </h2>
                                <ul className="list-disc pl-6 space-y-2">
                                    {step.content.map((item, itemIndex) => (
                                        <li key={itemIndex} className="text-white/90">
                                            {item}
                                        </li>
                                    ))}
                                </ul>
                            </section>
                        ))}
                    </div>

                    <p className="mt-8 text-center font-semibold">
                        Thank you for participating in your college's democratic process!
                    </p>
                </article>

                <nav className="flex justify-end gap-4">
                    <button
                        onClick={handleCancel}
                        className="px-6 py-3 text-lg font-semibold text-blue-900 bg-white rounded-full hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 transition-all"
                        aria-label="Return to home page"
                    >
                        BACK TO HOME
                    </button>
                </nav>
            </div>
        </main>
    );
}

export default VotingInstructionss;