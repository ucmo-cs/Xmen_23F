import { QueryClient, QueryClientProvider } from "@tanstack/react-query"
import { ReactQueryDevtools } from "@tanstack/react-query-devtools"
import { useRef } from "react"

function Providers({ children }) {
	const queryClientRef = useRef()

	if (!queryClientRef.current) {
		queryClientRef.current = new QueryClient()
	}

	return (
		<QueryClientProvider client={queryClientRef.current}>
			{children}
			<ReactQueryDevtools initialIsOpen={false} />
		</QueryClientProvider>
	)
}

export default Providers
