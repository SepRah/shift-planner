export default function LoadingSpinner({
                                           text = "Loading…",
                                           fullPage = false
                                       }) {
    const containerClass = fullPage
        ? "d-flex justify-content-center align-items-center vh-100"
        : "d-flex justify-content-center";

    return (
        <div className={containerClass}>
            <div className="text-center">
                <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">{text}</span>
                </div>
                {text && (
                    <div className="mt-2 text-muted">
                        {text}
                    </div>
                )}
            </div>
        </div>
    );
}