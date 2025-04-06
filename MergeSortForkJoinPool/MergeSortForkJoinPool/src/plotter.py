import matplotlib.pyplot as plt

filename = 'src/results.txt'

x_values = []
y_values = []

with open(filename, 'r') as file:
    for line in file:
        parts = line.strip().split()
        if len(parts) == 2:
            try:
                x = int(parts[0])
                y = float(parts[1])
                x_values.append(x)
                y_values.append(y)
            except ValueError:
                print(f"blad w linii: {line.strip()}")

plt.figure(figsize=(10, 6))
plt.plot(x_values, y_values, marker='o', linestyle='-', color='b')
plt.xlabel('THRESHOLD')
plt.ylabel('Czas wykonania [ms]')
plt.title('Zależność czasu wykonania od wartości THRESHOLD')
plt.grid(True)
plt.tight_layout()
plt.show()