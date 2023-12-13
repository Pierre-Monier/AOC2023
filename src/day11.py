file_path = 'input/aoc11.txt'
with open(file_path, 'r') as file:
    lines = [line.strip() for line in file.readlines()]

    n, m = (len(lines[0]), len(lines))

    galaxies = []
    for i in range(n):
        for j in range(m):
            if lines[j][i] == '#':
                galaxies.append((i, j))

    empty_rows = [j for j in range(m) if '#' not in lines[j]]
    empty_columns = [i for i in range(n) if all(lines[j][i] != '#' for j in range(n))]

    def distance(a, b):
        i1, j1 = a
        i2, j2 = b

        si, ei = min(i1, i2), max(i1, i2)
        sj, ej = min(j1, j2), max(j1, j2)

        ans = 0
        for i in range(si, ei):
            ans += 1
            if i in empty_columns:
                ans += 1

        for j in range(sj, ej):
            ans += 1
            if j in empty_rows:
                ans += 1

        return ans
    
    ans = 0
    for i in range(len(galaxies)):
        for j in range(i + 1, len(galaxies)):
            ans += distance(galaxies[i], galaxies[j])

    print(ans)