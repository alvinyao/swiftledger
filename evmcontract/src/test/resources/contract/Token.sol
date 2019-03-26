pragma solidity ^0.4.12;

interface VerifyMultiSign {
    //@param  signedStrs：signature info
    function verifyMultiSign(address _from, address _to, uint amount, bytes signedStrs) external view returns (bool verifyResult);
}

contract Token {
    address ownerAddress;
    string tokenSymbol;
    uint totalSupplyAmount;
    uint count = 0;

    constructor (
        address _ownerAddr,
        string _tokenSymbol,
        uint _totalSupply
    ) public {
        ownerAddress = _ownerAddr;
        tokenSymbol = _tokenSymbol;
        totalSupplyAmount = _totalSupply;
        balanceMap[ownerAddress] = totalSupplyAmount;
    }

    mapping(address => uint) balanceMap;
    mapping(address => uint) frozenAddressAmount;

    function getRandomNumber() public view returns (uint randomNumber){
        return count;
    }

    function getFrozenAmount(address freezeAddr) private view returns (uint){
        return frozenAddressAmount[freezeAddr];
    }

    function balanceOf(address _owner) public view returns (uint balanceAmount, uint frozenAmount){
        balanceAmount = balanceMap[_owner];
        frozenAmount = getFrozenAmount(_owner);
        return (balanceAmount, frozenAmount);
    }

    function getSourceHash(address _from, address _to, uint amount) public view returns (bytes32 sourceHash){
        require(_from != 0x0, "from address is 0x0");
        require(_to != 0x0, "from address is 0x0");
        require(amount > 0, "The amount is less than or equal to 0");
        return sha256(abi.encodePacked(_from, _to, amount, count));
    }

    function transfer(address _to, uint256 _value, bool multiSignFlag, bytes signedStrs) public payable returns (bool){
        require(msg.sender != 0x0, "from address is 0x0");
        if (multiSignFlag) {
            VerifyMultiSign verifyMultiSign = VerifyMultiSign(msg.sender);
            require(verifyMultiSign.verifyMultiSign(msg.sender, _to, _value, signedStrs));
            return transferFrom(msg.sender, _to, _value);
        }
        bytes32 sourceHash = getSourceHash(msg.sender, _to, _value);
        require(msg.sender == recovery(signedStrs, sourceHash), "sender signature verification failed");
        return transferFrom(msg.sender, _to, _value);
    }

    function transferFrom(address _from, address _to, uint256 _value) private returns (bool){
        require(_to != 0x0, "to address is 0x0");
        require(_value > 0, "the value must be that is greater than zero.");
        require(balanceMap[_from] - getFrozenAmount(_from) >= _value, "balance not enough");
        require(balanceMap[_to] + _value >= balanceMap[_to], "to address balance overflow");
        uint previousBalance = balanceMap[_from] + balanceMap[_to];
        balanceMap[_from] -= _value;
        balanceMap[_to] += _value;
        assert(balanceMap[_from] + balanceMap[_to] == previousBalance);
        count++;
        return true;
    }

    function recovery(bytes sig, bytes32 hash) public pure returns (address) {
        bytes32 r;
        bytes32 s;
        uint8 v;
        //Check the signature length
        require(sig.length == 65, "signature length not match");

        // Divide the signature in r, s and v variables
        assembly {
            r := mload(add(sig, 32))
            s := mload(add(sig, 64))
            v := byte(0, mload(add(sig, 96)))
        }
        // Version of signature should be 27 or 28
        if (v < 27) {
            v += 27;
        }
        //check version
        if (v != 27 && v != 28) {
            return address(0);
        }
        return ecrecover(hash, v, r, s);
    }
}