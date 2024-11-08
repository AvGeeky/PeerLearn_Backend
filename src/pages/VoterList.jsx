import React from 'react';
import { ArrowLeft, Users } from 'lucide-react';

const voterData = [
    { hashId: 'ABC123', status: 'VOTED' },
    { hashId: 'BEC344', status: 'NOT VOTED' },
    { hashId: 'HSJB223', status: 'NOT VOTED' },
    { hashId: 'DEF223', status: 'VOTED' }
];

function VoterRow({ hashId, status }) {
    return (
        <div className="flex justify-between items-center px-6 py-4 bg-white/10 backdrop-blur-sm rounded-lg transition-all duration-300 hover:bg-white/20 hover:transform hover:scale-102">
            <p className="text-white text-lg font-medium">{hashId}</p>
            <p className={`px-4 py-2 rounded-full text-sm font-bold ${status === 'VOTED' ? 'bg-green-500 text-white' : 'bg-yellow-500 text-gray-900'}`}>
                {status}
            </p>
        </div>
    );
}

function VoterList() {
    return (
        <main className="min-h-screen w-screen flex flex-col items-center justify-center bg-gradient-to-b from-[#FF4E6E] via-[#DA5F9C] to-[#2E33D1] overflow-hidden px-4 py-12">
            <div className="w-full max-w-5xl bg-white/10 backdrop-blur-md rounded-3xl p-8 md:p-12 shadow-2xl">
                <header className="text-center mb-12">
                    <h1 className="text-5xl md:text-6xl font-bold text-white mb-6 tracking-tight">
                        ELECTION NAME
                    </h1>
                    <p className="text-xl md:text-2xl text-white/90 font-light">
                        Real-time voter data from the database
                    </p>
                </header>

                <section className="w-full mb-12">
                    <div className="flex justify-between items-center px-8 py-5 bg-white/20 backdrop-blur-sm rounded-xl mb-6">
                        <h2 className="text-xl font-semibold text-white flex items-center">
                            <Users className="mr-2" /> HASH-ID
                        </h2>
                        <h2 className="text-xl font-semibold text-white">Status</h2>
                    </div>

                    <div className="space-y-4">
                        {voterData.map((voter) => (
                            <VoterRow
                                key={voter.hashId}
                                hashId={voter.hashId}
                                status={voter.status}
                            />
                        ))}
                    </div>
                </section>

                <nav className="flex justify-center">
                    <a
                        href="/"
                        className="px-8 py-4 text-lg font-semibold text-blue-900 bg-white rounded-full hover:bg-blue-50 focus:outline-none focus:ring-4 focus:ring-white focus:ring-offset-2 focus:ring-offset-blue-600 transition-all duration-300 flex items-center"
                    >
                        <ArrowLeft className="mr-2" /> BACK TO HOME
                    </a>
                </nav>
            </div>
        </main>
    );
}

export default VoterList;