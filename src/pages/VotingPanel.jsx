import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ChevronDown, Check } from 'lucide-react';

const candidateCodes = ['A', 'B', 'C', 'D', 'E', 'F', 'G'];

function CandidateCard({ candidate, isSelected, onSelect }) {
    return (
        <article
            className={`flex justify-between items-center p-4 w-full bg-white/10 backdrop-blur-md rounded-lg cursor-pointer border-2 transition-all duration-200 ${
                isSelected ? 'border-blue-500 bg-white/20' : 'border-transparent hover:bg-white/15'
            }`}
            onClick={onSelect}
        >
            <div className="flex items-center gap-4">
                <div
                    className={`w-6 h-6 rounded-full border-2 flex items-center justify-center ${
                        isSelected ? 'bg-blue-500 border-blue-500' : 'border-gray-300'
                    }`}
                >
                    {isSelected && <Check className="w-4 h-4 text-white" />}
                </div>
                <span className="text-xl text-white">{candidate.name}</span>
            </div>
            <div className="flex items-center gap-8">
                <span className="text-xl text-white">{candidate.party}</span>
                <span className="text-gray-300 font-semibold">Code: {candidate.code}</span>
                <ChevronDown className="w-6 h-6 text-gray-300" />
            </div>
        </article>
    );
}

export default function VotingPanel() {
    const [selectedCandidate, setSelectedCandidate] = useState(null);
    const [enteredCode, setEnteredCode] = useState('');
    const [agreeTerms, setAgreeTerms] = useState(false);
    const navigate = useNavigate();

    const [candidates] = useState([
        {
            name: 'Dr. Ramcharan Swaminathan',
            party: 'BerryBoys',
            code: 'A',
            manifesto: 'Committed to sustainable agriculture and economic growth through innovative berry farming techniques.',
        },
        {
            name: 'Rahul',
            party: 'BJP',
            code: 'B',
            manifesto: 'Focused on national security, economic reforms, and cultural preservation.',
        },
        {
            name: 'Rahul Gandhi',
            party: 'Congress',
            code: 'C',
            manifesto: 'Advocating for inclusive growth, social justice, and strengthening democratic institutions.',
        },
        {
            name: 'BATMAN',
            party: 'GCPD',
            code: 'D',
            manifesto: 'Saving Gotham city just like nithin saving CSE B',
        },
    ]);

    const handleCodeChange = (e) => {
        setEnteredCode(e.target.value.toUpperCase());
    };

    const handleSubmit = () => {
        if (!selectedCandidate) {
            alert('Please select a candidate before submitting your vote.');
            return;
        }

        if (enteredCode !== selectedCandidate.code) {
            alert('The entered code does not match the selected candidate. Please check and try again.');
            return;
        }

        if (!agreeTerms) {
            alert('Please agree to the terms before submitting your vote.');
            return;
        }

        alert(`Vote submitted successfully for ${selectedCandidate.name} from ${selectedCandidate.party}`);
        navigate('/voter-list');
    };

    return (
        <main className="min-h-screen w-screen flex items-center justify-center bg-gradient-to-b from-[#FF4E6E] via-[#DA5F9C] to-[#2E33D1] overflow-hidden px-4 py-8">
            <div className="w-full max-w-4xl bg-black/40 backdrop-blur-md rounded-xl p-8 shadow-2xl">
                <h1 className="text-4xl font-bold text-white mb-8 text-center">Official Voting Panel</h1>

                <div className="space-y-6">
                    {selectedCandidate && (
                        <div className="bg-white/20 backdrop-blur-md p-6 rounded-lg mb-6">
                            <div className="flex justify-between items-center text-white mb-4">
                                <div className="flex items-center gap-4">
                                    <div className="w-10 h-10 bg-blue-500 rounded-full flex items-center justify-center">
                                        <span className="text-white font-bold">{selectedCandidate.code}</span>
                                    </div>
                                    <span className="text-2xl font-semibold">{selectedCandidate.name}</span>
                                </div>
                                <span className="text-xl">{selectedCandidate.party}</span>
                            </div>
                            <div className="bg-white/10 backdrop-blur-md p-4 rounded-lg text-white">
                                <p className="mb-4">
                                    <strong>Manifesto:</strong> {selectedCandidate.manifesto}
                                </p>
                                <div>
                                    <label htmlFor="codeInput" className="block text-white mb-2">Enter the unique code for this candidate:</label>
                                    <input
                                        id="codeInput"
                                        type="text"
                                        value={enteredCode}
                                        onChange={handleCodeChange}
                                        className="w-full p-2 border rounded text-black"
                                        maxLength={1}
                                        placeholder="Enter code"
                                    />
                                </div>
                            </div>
                        </div>
                    )}

                    <div className="space-y-2">
                        {candidates.map((candidate, index) => (
                            <CandidateCard
                                key={index}
                                candidate={candidate}
                                isSelected={selectedCandidate?.name === candidate.name}
                                onSelect={() => setSelectedCandidate(candidate)}
                            />
                        ))}
                    </div>

                    <div className="flex items-center gap-4 text-white mt-6">
                        <input
                            type="checkbox"
                            id="agreeTerms"
                            className="w-5 h-5"
                            checked={agreeTerms}
                            onChange={(e) => setAgreeTerms(e.target.checked)}
                        />
                        <label htmlFor="agreeTerms">
                            I confirm that I have selected my candidate and the information provided is correct.
                        </label>
                    </div>
                    <div className="flex justify-center mt-8">
                        <button
                            onClick={handleSubmit}
                            className="inline-block px-8 py-3 text-lg font-semibold text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-all duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 disabled:opacity-50 disabled:cursor-not-allowed"
                            disabled={!selectedCandidate || !agreeTerms || enteredCode !== selectedCandidate?.code}
                        >
                            SUBMIT VOTE
                        </button>
                    </div>
                </div>
            </div>
        </main>
    );
}