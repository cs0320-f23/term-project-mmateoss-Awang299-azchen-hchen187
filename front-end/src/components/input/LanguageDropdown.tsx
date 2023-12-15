import React from 'react';
import Select, { ActionMeta, SingleValue } from 'react-select';
import { useAppContext } from './ContextProvider';

//interface that outlines the shape of the data
interface Language {
  value: string;
  label: string;
}

//interface that specifies values passed into the component
interface LanguageDropdownProps {
  setSongLang: boolean;
}

//main component of the dropdown selection
const LanguageDropdown = (props: LanguageDropdownProps) => {
  const {chooseNativeLanguage, chooseSongLanguage} = useAppContext();
  
  //method to update the useContext with the language chosen
  const handleLanguageSelection = (
    selectedOption: SingleValue<Language>,
    actionMeta: ActionMeta<Language>
  ) => {
    if (selectedOption) {
      if (props.setSongLang) {
        chooseSongLanguage(selectedOption.label)
      } else chooseNativeLanguage(selectedOption.label);
    }
  };

  //list of language options
  const options: Language[] = [
    { value: 'en', label: 'English' },
    { value: 'es', label: 'Spanish' },
    { value: 'fr', label: 'French' },
    { value: 'de', label: 'German' },
    { value: 'it', label: 'Italian' },
    { value: 'ir', label: 'Irish' },
    { value: 'in', label: 'Indonesian' },
    { value: 'pl', label: 'Polish' },
    { value: 'po', label: 'Portuguese' },
    { value: 'ro', label: 'Romanian' },
    { value: 'tu', label: 'Turkish' },
    { value: 'fi', label: 'Filipino' },
    { value: 'ma', label: 'Malay' },
  ];

  //returns the component 
  return (
    <Select
      options={options}
      placeholder="Select a language"
      onChange={handleLanguageSelection}
    />
  );
};

export default LanguageDropdown;