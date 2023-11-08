import axios from "axios"
import { useParams } from "react-router-dom"
import apiFetch from "../utils/apiFetch"
import { useQuery } from "@tanstack/react-query"
function ChangeRequestPage() {
	const params = useParams()
	const { isError, isLoading, data } = useQuery({
		queryKey: ["change-request"],
		queryFn: async () =>
			await apiFetch("GET", `/api/v1/change/${params.id}`)
				.then(res => res.data)
				.catch(err => console.log(err)),
	})

	if (isLoading) return <p>Loading...</p>
	if (isError) return <p>Error...</p>

	return (
		<div>
			<p>{JSON.stringify(data)}</p>
		</div>
	)
}

export default ChangeRequestPage
