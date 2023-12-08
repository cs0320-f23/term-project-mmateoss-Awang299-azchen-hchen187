import React from 'react';
import Select, { ActionMeta, SingleValue } from 'react-select';
import { useAppContext } from './ContextProvider';

interface Language {
  value: string;
  label: string;
}

const LanguageDropdown = () => {
  const {nativeLanguage, chooseNativeLanguage} = useAppContext();
  
  const handleLanguageSelection = (
    selectedOption: SingleValue<Language>,
    actionMeta: ActionMeta<Language>
  ) => {
    if (selectedOption) {
        chooseNativeLanguage(selectedOption.label);
    }
  };

  const options: Language[] = [
    { value: 'en', label: 'English' },
    { value: 'es', label: 'Spanish' },
    { value: 'fr', label: 'French' },
    { value: 'de', label: 'German' },
    { value: 'ch', label: 'Chinese' },
    { value: 'en', label: 'English' },
    { value: 'es', label: 'Spanish' },
    { value: 'fr', label: 'French' },
    { value: 'de', label: 'German' },
    { value: 'ch', label: 'Chinese' },
  ];

  return (
    <Select
      options={options}
      placeholder="Select a language"
      className={nativeLanguage ? nativeLanguage : "Select a language"}
      onChange={handleLanguageSelection}
    />
  );
};

export default LanguageDropdown;