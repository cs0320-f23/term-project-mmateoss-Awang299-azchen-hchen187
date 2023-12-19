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
    { value: 'af', label: 'Afrikaans' },
    { value: 'sq', label: 'Albanian' },
    { value: 'am', label: 'Amharic' },
    { value: 'ar', label: 'Arabic' },
    { value: 'hy', label: 'Armenian' },
    { value: 'as', label: 'Assamese' },
    { value: 'az', label: 'Azerbaijani (Latin)' },
    { value: 'az', label: 'Azerbaijani' },
    { value: 'bn', label: 'Bangla' },
    { value: 'ba', label: 'Bashkir' },
    { value: 'eu', label: 'Basque' },
    { value: 'bho', label: 'Bhojpuri' },
    { value: 'brx', label: 'Bodo' },
    { value: 'bs', label: 'Bosnian' },
    { value: 'bg', label: 'Bulgarian' },
    { value: 'yue', label: 'Cantonese' },
    { value: 'ca', label: 'Catalan' },
    { value: 'lzh', label: 'Chinese (Literary)' },
    { value: 'zh-Hans', label: 'Chinese Simplified' },
    { value: 'zh-Hant', label: 'Chinese Traditional' },
    { value: 'sn', label: 'Chishona' },
    { value: 'hr', label: 'Croatian' },
    { value: 'cs', label: 'Czech' },
    { value: 'da', label: 'Danish' },
    { value: 'prs', label: 'Dari' },
    { value: 'dv', label: 'Divehi' },
    { value: 'doi', label: 'Dogri' },
    { value: 'nl', label: 'Dutch' },
    { value: 'en', label: 'English' },
    { value: 'et', label: 'Estonian' },
    { value: 'fo', label: 'Faroese' },
    { value: 'fj', label: 'Fijian' },
    { value: 'fil', label: 'Filipino' },
    { value: 'fi', label: 'Finnish' },
    { value: 'fr', label: 'French' },
    { value: 'fr-ca', label: 'French (Canada)' },
    { value: 'gl', label: 'Galician' },
    { value: 'ka', label: 'Georgian' },
    { value: 'de', label: 'German' },
    { value: 'el', label: 'Greek' },
    { value: 'gu', label: 'Gujarati' },
    { value: 'ht', label: 'Haitian Creole' },
    { value: 'ha', label: 'Hausa' },
    { value: 'he', label: 'Hebrew' },
    { value: 'hi', label: 'Hindi' },
    { value: 'mww', label: 'Hmong Daw (Latin)' },
    { value: 'hu', label: 'Hungarian' },
    { value: 'is', label: 'Icelandic' },
    { value: 'ig', label: 'Igbo' },
    { value: 'id', label: 'Indonesian' },
    { value: 'ikt', label: 'Inuinnaqtun' },
    { value: 'iu', label: 'Inuktitut' },
    { value: 'iu-Latn', label: 'Inuktitut (Latin)' },
    { value: 'ga', label: 'Irish' },
    { value: 'it', label: 'Italian' },
    { value: 'ja', label: 'Japanese' },
    { value: 'kn', label: 'Kannada' },
    { value: 'ks', label: 'Kashmiri' },
    { value: 'kk', label: 'Kazakh' },
    { value: 'km', label: 'Khmer' },
    { value: 'rw', label: 'Kinyarwanda' },
    { value: 'tlh-Latn', label: 'Klingon' },
    { value: 'tlh-Piqd', label: 'Klingon (Piqd)' },
    { value: 'gom', label: 'Konkani' },
    { value: 'ko', label: 'Korean' },
    { value: 'ku', label: 'Kurdish (Central)' },
    { value: 'kmr', label: 'Kurdish (Northern)' },
    { value: 'ku', label: 'Kurdish' },
    { value: 'ky', label: 'Kyrgyz (Cyrillic)' },
    { value: 'ky', label: 'Kyrgyz' },
    { value: 'lo', label: 'Lao' },
    { value: 'lv', label: 'Latvian' },
    { value: 'lt', label: 'Lithuanian' },
    { value: 'ln', label: 'Lingala' },
    { value: 'dsb', label: 'Lower Sorbian' },
    { value: 'lug', label: 'Luganda' },
    { value: 'mk', label: 'Macedonian' },
    { value: 'mai', label: 'Maithili' },
    { value: 'mg', label: 'Malagasy' },
    { value: 'ms', label: 'Malay (Latin)' },
    { value: 'ms', label: 'Malay' },
    { value: 'ml', label: 'Malayalam' },
    { value: 'mt', label: 'Maltese' },
    { value: 'mi', label: 'Maori' },
    { value: 'mr', label: 'Marathi' },
    { value: 'mn-Cyrl', label: 'Mongolian (Cyrillic)' },
    { value: 'mn-Mong', label: 'Mongolian (Traditional)' },
    { value: 'mn-Cyrl', label: 'Mongolian' },
    { value: 'my', label: 'Myanmar' },
    { value: 'ne', label: 'Nepali' },
    { value: 'nb', label: 'Norwegian' },
    { value: 'nya', label: 'Nyanja' },
    { value: 'or', label: 'Odia' },
    { value: 'ps', label: 'Pashto' },
    { value: 'fa', label: 'Persian' },
    { value: 'pl', label: 'Polish' },
    { value: 'pt', label: 'Portuguese (Brazil)' },
    { value: 'pt-pt', label: 'Portuguese (Portugal)' },
    { value: 'pt', label: 'Portuguese' },
    { value: 'pa', label: 'Punjabi' },
    { value: 'otq', label: 'Queretaro Otomi' },
    { value: 'ro', label: 'Romanian' },
    { value: 'run', label: 'Rundi' },
    { value: 'ru', label: 'Russian' },
    { value: 'sm', label: 'Samoan' },
    { value: 'sr-Cyrl', label: 'Serbian (Cyrillic)' },
    { value: 'sr-Latn', label: 'Serbian (Latin)' },
    { value: 'sr-Latn', label: 'Serbian' },
    { value: 'st', label: 'Sesotho' },
    { value: 'nso', label: 'Sesotho Sa Leboa' },
    { value: 'tn', label: 'Setswana' },
    { value: 'sd', label: 'Sindhi' },
    { value: 'si', label: 'Sinhala' },
    { value: 'sk', label: 'Slovak' },
    { value: 'sl', label: 'Slovenian' },
    { value: 'so', label: 'Somali (Arabic)' },
    { value: 'so', label: 'Somali' },
    { value: 'es', label: 'Spanish' },
    { value: 'sw', label: 'Swahili (Latin)' },
    { value: 'sw', label: 'Swahili' },
    { value: 'sv', label: 'Swedish' },
    { value: 'ty', label: 'Tahitian' },
    { value: 'ta', label: 'Tamil' },
    { value: 'tt', label: 'Tatar (Latin)' },
    { value: 'tt', label: 'Tatar' },
    { value: 'te', label: 'Telugu' },
    { value: 'th', label: 'Thai' },
    { value: 'bo', label: 'Tibetan' },
    { value: 'ti', label: 'Tigrinya' },
    { value: 'to', label: 'Tongan' },
    { value: 'tr', label: 'Turkish' },
    { value: 'tk', label: 'Turkmen' },
    { value: 'uk', label: 'Ukrainian' },
    { value: 'hsb', label: 'Upper Sorbian' },
    { value: 'ur', label: 'Urdu' },
    { value: 'ug', label: 'Uyghur (Arabic)' },
    { value: 'ug', label: 'Uyghur' },
    { value: 'uz', label: 'Uzbek (Latin)' },
    { value: 'uz', label: 'Uzbek' },
    { value: 'vi', label: 'Vietnamese' },
    { value: 'cy', label: 'Welsh' },
    { value: 'xh', label: 'Xhosa' },
    { value: 'yo', label: 'Yoruba' },
    { value: 'yua', label: 'Yucatec Maya' },
    { value: 'zu', label: 'Zulu' },
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