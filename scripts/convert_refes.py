
import pandas as pd
import json
import os

def convert_refes(csv_path, output_json_path):
    print(f"Leyendo CSV: {csv_path}")
    # Read the CSV with correct encoding (usually utf-8 or latin-1 for AR data)
    # Based on the head output, it seems comma separated with quotes
    try:
        df = pd.read_csv(csv_path, encoding='utf-8')
    except UnicodeDecodeError:
        df = pd.read_csv(csv_path, encoding='latin-1')

    print(f"Registros encontrados: {len(df)}")
    
    # Selecting relevant columns
    # 'provincia_nombre', 'departamento_nombre', 'establecimiento_nombre'
    cols = ['provincia_nombre', 'departamento_nombre', 'establecimiento_nombre']
    
    # Cleaning: remove nulls and trim whitespaces
    df = df[cols].dropna()
    for col in cols:
        df[col] = df[col].astype(str).str.strip()

    # Sort to ensure hierarchy is clean
    df = df.sort_values(by=cols)

    # Building the hierarchy
    provinces_list = []
    
    for prov_name, prov_group in df.groupby('provincia_nombre'):
        province_obj = {
            "name": prov_name,
            "departments": []
        }
        
        for dept_name, dept_group in prov_group.groupby('departamento_nombre'):
            dept_obj = {
                "name": dept_name,
                "establishments": sorted(dept_group['establecimiento_nombre'].unique().tolist())
            }
            province_obj["departments"].append(dept_obj)
        
        provinces_list.append(province_obj)

    final_data = {"provinces": provinces_list}

    print(f"Escribiendo JSON em: {output_json_path}")
    with open(output_json_path, 'w', encoding='utf-8') as f:
        json.dump(final_data, f, ensure_ascii=False, indent=4)
    
    print("Conversión completada con éxito.")

if __name__ == "__main__":
    csv_input = r"C:\Users\jsanc\Downloads\establecimientos-asistenciales-asentados-registro-federal-refes-20251215.csv"
    json_output = r"C:\Users\jsanc\Proyectos IA\ReEpiTer\android\app\src\main\assets\refes_data.json"
    
    if os.path.exists(csv_input):
        convert_refes(csv_input, json_output)
    else:
        print(f"Error: No se encuentra el archivo en {csv_input}")
