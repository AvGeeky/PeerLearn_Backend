import React from "react";

const candidates = [
    { name: "Ramcharan", votes: 5 },
    { name: "Rahul", votes: 4 },
    { name: "Rahul Gandhi", votes: 3 },
    { name: "Batman", votes: 2 },
];

function CandidateRow({ name, votes }) {
    return (
        <section className="flex justify-between px-4 py-3 mt-2 w-full rounded-md bg-white/20 backdrop-blur-sm">
            <div className="text-white">{name}</div>
            <div className="text-white">{votes}</div>
        </section>
    );
}

function ElectionResults() {
    return (
        <main className="min-h-screen w-screen flex flex-col items-center justify-center bg-gradient-to-b from-[#FF4E6E] via-[#DA5F9C] to-[#2E33D1] overflow-hidden px-4 py-8">
            <div className="w-full max-w-4xl bg-white/10 backdrop-blur-md rounded-xl p-8 shadow-2xl">
                <h1 className="text-5xl font-bold text-white mb-4 text-center">
                    ELECTION NAME
                </h1>
                <h2 className="text-3xl text-white mb-8">
                    Results
                </h2>
                <section className="flex justify-between px-4 py-3 mb-4 w-full rounded-md bg-white/30 backdrop-blur-sm font-semibold">
                    <div className="text-white">Candidate</div>
                    <div className="text-white">Votes</div>
                </section>
                {candidates.map((candidate, index) => (
                    <CandidateRow key={index} name={candidate.name} votes={candidate.votes} />
                ))}
                <div className="flex justify-center mt-8">
                    <a
                        href="/"
                        className="px-6 py-3 text-lg font-semibold text-blue-900 bg-white rounded-full hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 transition-all"
                    >
                        BACK TO HOME
                    </a>
                </div>
            </div>
        </main>
    );
}

export default ElectionResults;