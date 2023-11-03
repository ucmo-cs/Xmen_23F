import { useState } from "react"
import { useQuery } from "@tanstack/react-query"
import { useNavigate } from "react-router-dom"
import apiFetch from "../utils/apiFetch"
import dayjs from "dayjs"
import relativeTime from "dayjs/plugin/relativeTime"

dayjs.extend(relativeTime)

function Dashboard() {
	const { data, isError, isLoading } = useQuery({
		queryKey: ["dashboard"],
		queryFn: async () => {
			const res = await apiFetch("GET", "/api/v1/change")
			console.log(res.data)
			return res.data.content
		},
	})

	if (isLoading) {
		//https://flowbite.com/docs/components/spinner/
		return (
			<div>
				<div className="flex justify-center place-items-center w-44 h-44">
					<div role="status">
						<svg
							aria-hidden="true"
							className="inline w-8 h-8 mr-2 text-gray-200 animate-spin dark:text-gray-600 fill-green-500"
							viewBox="0 0 100 101"
							fill="none"
							xmlns="http://www.w3.org/2000/svg">
							<path
								d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
								fill="currentColor"
							/>
							<path
								d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
								fill="currentFill"
							/>
						</svg>
						<span className="sr-only">Loading...</span>
					</div>
				</div>
			</div>
		)
	}

	return (
		//	https://flowbite.com/docs/components/tables/
		<div>
			<NavBar />
			<div className="relative overflow-x-auto shadow-md sm:rounded-lg">
				<table className="w-full text-sm text-left text-gray-500">
					<thead className="text-xs text-gray-700 uppercase bg-gray-50">
						<tr>
							<th scope="col" className="px-6 py-3">
								Id
							</th>
							<th scope="col" className="px-6 py-3">
								Application Id
							</th>
							<th scope="col" className="px-6 py-3">
								Change Type
							</th>
							<th scope="col" className="px-6 py-3">
								Date
							</th>
						</tr>
					</thead>
					<tbody className="bg-white">
						{data.map(change => (
							<tr className="bg-white" key={change.id}>
								<th
									scope="row"
									className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
									{change.id}
								</th>
								<td
									className="px-6 py-4			console.log(res.data)
 ">
									{change.applicationId}
								</td>
								<td className="px-6 py-4 ">{change.changeType}</td>
								<td className="px-6 py-4 ">
									{dayjs(change.dateCreated).format("DD/MM/YYYY")}
								</td>
							</tr>
						))}
					</tbody>
				</table>
			</div>
		</div>
	)
}

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
					<a className="flex items-center">
						<img
							src="cblogowhite00c2aa7d.svg"
							className="h-12 mr-3"
							alt="Commerce Bank Logo"
						/>
					</a>
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

export default Dashboard
