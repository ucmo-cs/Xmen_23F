import { useState } from "react"
import { useNavigate, Link } from "react-router-dom"
import CBNavImage from "@/assets/cblogowhite00c2aa7d.svg"

function NavBar() {
	const [showNav, setShowNav] = useState(false)
	const navigate = useNavigate()

	const toggleNav = () => {
		setShowNav(!showNav)
	}

	return (
		<div>
			<nav className="bg-green-800 relative">
				<div className="max-w-screen flex flex-wrap items-center justify-between mx-auto p-4">
					<Link className="flex items-center" to={"/dashboard"}>
						<img src={CBNavImage} alt="logo" className="h-12 mr-3" />
					</Link>
					<button
						onClick={toggleNav}
						className="inline-flex items-center p-2 w-12 h-12 justify-center text-sm text-gray-500 rounded-lg md:hidden focus:outline-none focus:ring-2 dark:text-gray-400"
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

					{/* The responsive navigation controlled by the showNav state */}
					<div
						className={`w-full md:block md:w-auto ${showNav ? "" : "hidden"}`}
						id="navbar-default">
						<ul className="font-medium md:text-lg flex flex-col mx-2 my-4 p-1 md:p-0 border border-gray-100 rounded-lg md:flex-row md:space-x-8 md:mt-0 md:border-0 bg-slate-300 md:bg-transparent text-center">
							{/* Your navigation links */}
							<li className="m-0.5">
								<a
									href="http://localhost:5173/dashboard#"
									className=" block py-2 pl-3 pr-4 text-black md:text-white font-bold bg-slate-200 rounded md:bg-transparent md:p-0"
									aria-current="page">
									Dashboard
								</a>
							</li>
							<li className="m-0.5">
								<button
									onClick={() => {
										localStorage.removeItem("token")
										navigate("/")
									}}
									className="block py-2 pl-3 pr-4 text-black md:text-white font-bold bg-slate-100 rounded md:bg-transparent md:p-0 hover:bg-slate-200"
									aria-current="page">
									Sign Out
								</button>
							</li>
						</ul>
					</div>
				</div>
			</nav>
		</div>
	)
}

export default NavBar
