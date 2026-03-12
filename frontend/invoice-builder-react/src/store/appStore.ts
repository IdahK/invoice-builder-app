import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import type { AppConfig } from '../types';
import { setPDFMode, type PDFGenerationMode } from '../services/pdfService';

interface AppStore extends AppConfig {
  setDarkMode: (darkMode: boolean) => void;
  toggleDarkMode: () => void;
  setPDFGenerationMode: (mode: PDFGenerationMode) => void;
}

export const useAppStore = create<AppStore>()(
  persist(
    (set, get) => ({
      pdfGenerationMode: 'client',
      darkMode: false,
      setDarkMode: (darkMode: boolean) => {
        set({ darkMode });
        if (darkMode) {
          document.documentElement.classList.add('dark');
        } else {
          document.documentElement.classList.remove('dark');
        }
      },
      toggleDarkMode: () => {
        const newMode = !get().darkMode;
        get().setDarkMode(newMode);
      },
      setPDFGenerationMode: (mode: PDFGenerationMode) => {
        setPDFMode(mode);
        set({ pdfGenerationMode: mode });
      },
    }),
    {
      name: 'invoice-builder-config',
      onRehydrateStorage: () => (state) => {
        if (state?.darkMode) {
          document.documentElement.classList.add('dark');
        }
        if (state?.pdfGenerationMode) {
          setPDFMode(state.pdfGenerationMode);
        }
      },
    }
  )
);
