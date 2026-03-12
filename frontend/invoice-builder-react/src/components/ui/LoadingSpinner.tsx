export default function LoadingSpinner() {
  return (
    <div className="flex items-center justify-center py-12">
      <div className="w-8 h-8 border-4 border-t-violet-600 rounded-full animate-spin border-gray-200 dark:border-slate-700" />
    </div>
  );
}
