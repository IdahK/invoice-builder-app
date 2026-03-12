import { useAppStore } from '../../store/appStore';

export default function LoadingSpinner() {
  const darkMode = useAppStore((state) => state.darkMode);

  return (
    <div className="flex items-center justify-center py-12">
      <div className={`w-8 h-8 border-4 border-t-violet-600 rounded-full animate-spin ${
        darkMode ? 'border-slate-700' : 'border-gray-200'
      }`} />
    </div>
  );
}
