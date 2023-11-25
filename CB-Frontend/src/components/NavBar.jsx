import React, { useState, useEffect } from "react"
import { Link, useNavigate } from "react-router-dom"
import cblogowhite from "../assets/cblogowhite00c2aa7d.svg"
import greenlogo from "../assets/greenlogo.png"

function NavBar() {
	const [showNav, setShowNav] = useState(false)
	const toggleNav = () => {
		setShowNav(!showNav)
	}

	const navigate = useNavigate()

	const [innerWidth, setInnerWidth] = useState(window.innerWidth)

	useEffect(() => {
		const handleResize = () => {
			setInnerWidth(window.innerWidth)
		}

		window.addEventListener("resize", handleResize)
		return () => window.removeEventListener("resize", handleResize)
	}, []) // Run only on mount and unmount

	return (
		<div>
			<nav
				className={`relative ${
					innerWidth < 768 ? "bg-green-800" : "bg-slate-200"
				}`}>
				<div className="max-w-screen flex flex-wrap items-center p-2 relative">
					{/* Displayed on screens smaller than 768px */}
					<Link
						to="/dashboard"
						className={`${
							innerWidth < 768 ? "flex" : "hidden"
						} items-center mr-auto`}>
						<img
							src={cblogowhite}
							className="h-12 mr-3"
							alt="Commerce Bank Logo"
						/>
					</Link>

					{/* Displayed on screens 768px and larger */}
					<Link
						to="/dashboard"
						className={`${
							innerWidth >= 768 ? "flex" : "hidden"
						} items-center mr-auto`}>
						<img
							src={greenlogo}
							className="h-12 mr-3"
							alt="Commerce Bank Logo"
						/>
					</Link>

					<div className="flex">
						{/* "Submit Request" link */}
						<Link
							to="/change-request"
							className={`${
								innerWidth >= 768 ? "flex" : "hidden"
							} items-center mr-4 py-2 pl-3 pr-4 text-black font-bold bg-slate-100 hover:bg-slate-200 transition duration-300 ease-in-out`}
							aria-current="page">
							Submit Request
						</Link>

						{/* "Dashboard" link */}
						<Link
							to="/dashboard"
							className={`${
								innerWidth >= 768 ? "flex" : "hidden"
							} items-center mr-4 py-2 pl-3 pr-4 text-black font-bold bg-slate-200 hover:bg-slate-200`}
							aria-current="page">
							Dashboard
						</Link>

						<button
							onClick={() => {
								localStorage.removeItem("token")
								navigate("/")
							}}
							className={`${
								innerWidth >= 768 ? "text-black" : "text-white"
							} block py-2 px-4 border-x-2 border-slate-500 bg-transparent hover:bg-slate-300 transition duration-300 ease-in-out`}
							aria-current="page">
							Sign Out
						</button>

						<button
							onClick={toggleNav}
							className="inline-flex items-center m-2 w-12 h-12 justify-center text-sm text-gray-500 rounded-lg md:hidden focus:outline-none focus:ring-2 dark:text-gray-400"
							aria-controls="navbar-default"
							aria-expanded={showNav}>
							<span className="sr-only">Open main menu</span>
							<svg
								className="w-5 h-5"
								aria-hidden="true"
								xmlns="http://www.w3.org/2000/svg"
								fill="none"
								viewBox="0 0 17 14">
								<path
									stroke="currentColor"
									strokeLinecap="round"
									strokeLinejoin="round"
									strokeWidth="2"
									d="M1 1h15M1 7h15M1 13h15"
								/>
							</svg>
						</button>
					</div>

					{/* The responsive navigation controlled by the showNav state */}
					{showNav && (
						<div
							className={`${
								innerWidth < 768 ? "absolute block" : "hidden"
							} max-w-screen top-full left-0 right-0 bg-white border border-gray-100 rounded-md shadow-md mt-2 z-10`}
							id="navbar-dropdown">
							<ul className="font-medium md:text-lg flex flex-col p-2 space-y-2">
								<li>
									<Link
										to="/change-request"
										className={`${
											innerWidth >= 768
												? "bg-transparent"
												: "bg-transparent hover:bg-slate-200"
										} block w-full py-2 pl-3 pr-4 text-black font-bold transition duration-300 ease-in-out"`}
										aria-current="page">
										Submit Request
									</Link>
								</li>
								<li>
									<Link
										to="/dashboard"
										className="block w-full py-2 pl-3 pr-4 text-black font-bold bg-slate-200 hover:bg-slate-200"
										aria-current="page">
										Dashboard
									</Link>
								</li>
							</ul>
						</div>
					)}
				</div>
			</nav>
		</div>
	)
}

export default NavBar
